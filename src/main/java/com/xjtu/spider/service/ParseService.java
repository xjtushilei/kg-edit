package com.xjtu.spider.service;

import com.xjtu.common.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 解析"百度百科"网页
 * <p>
 * Created by yuanhao on 2017/3/15.
 */
@Service
public class ParseService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将从“摘要”到各级标题的所有分面内容全部存到一起，获取term的所有碎片
     *
     * @param doc        知识点词条term对应的doc对象，两个标志位
     * @param flagFirst
     * @param flagSecond
     * @return 所有分面及其对应内容的映射
     */
    public HashMap<String, String> getAllContent(Document doc, boolean flagFirst, boolean flagSecond) {

        HashMap<String, String> map = new HashMap<String, String>();

        Elements mainContents = doc.select("div.main-content").select("span.title-prefix");
        if (mainContents.size() == 0) {
            HashMap<String, String> specialContent = getSpecialContent(doc); // 没有目录栏的词条信息
            map.putAll(specialContent);
        } else {
            HashMap<String, String> summaryContent = getSummary(doc); // 摘要内容
            map.putAll(summaryContent);
            if (flagFirst) {
                LinkedList<String> firstTitle = getFirstTitle(doc);
                if (firstTitle.size() != 0) {
                    HashMap<String, String> firstContent = getFirstContent(doc); // 一级分面内容
                    map.putAll(firstContent);
                }
            }
            if (flagSecond) {
                LinkedList<String> secondTitle = getSecondTitle(doc);
                if (secondTitle.size() != 0) {
                    HashMap<String, String> secondContent = getSecondContent(doc); // 二级分面内容
                    map.putAll(secondContent);
                }
            }
        }
        return map;
    }

    /**
     * 网页没有一级或者二级标题，获取所有内容
     *
     * @param doc
     * @return 所有标题及其所有内容的映射
     */
    public HashMap<String, String> getSpecialContent(Document doc) {
        HashMap<String, String> map = new HashMap<String, String>();
        Elements para = doc.select("div.main-content").select("div[label-module=para]");
        if (para.size() != 0) {
            String conAll = "";
            for (int i = 0; i < para.size(); i++) {
                String con = para.get(i).text();
                conAll += "\n" + con;
            }
            map.put("摘要", conAll.trim());
        }
        return map;
    }

    /**
     * 获取二级标题对应的内容
     *
     * @param doc
     * @return 二级标题列表及其对应内容列表的映射
     */
    public HashMap<String, String> getSecondContent(Document doc) {
        HashMap<String, String> secondContent = new HashMap<String, String>();
        LinkedList<String> allTitle = getAllTitle(doc);
        LinkedList<String> secondTitle = getSecondTitle(doc);
        LinkedList<Element> nodes = getNodes(doc);

        // 寻找一级和二级标题在节点链表的下标
        LinkedList<Integer> allTitleIndex = getAllTitleIndex(allTitle, nodes);

        // 比较标题链表和对应的下标链表的大小是否相同，原则上是相同的，不相同说明网页存在问题等。。。
        int len = allTitle.size();
        int indexLen = allTitleIndex.size();
        if (len > indexLen) {
            len = indexLen;
        }

        // 获取每个二级标题的内容，为该标题与相邻标题下标之间的节点内容
        for (int i = 0; i < len - 1; i++) {
            String title = allTitle.get(i);
            for (int j = 0; j < secondTitle.size(); j++) {
                String secTitle = secondTitle.get(j);
                if (title.equals(secTitle)) { // 遍历所有标题，寻找到二级标题
                    String content = "";
                    int begin = allTitleIndex.get(i);
                    int end = allTitleIndex.get(i + 1);
                    for (int k = begin + 1; k < end; k++) {
                        Element node = nodes.get(k);
                        content += "\n" + node.text();
                    }
                    secondContent.put(title, content.trim());
                }
            }
        }

        // 所有标题的最后一个标题是否为二级标题
        String title = allTitle.get(len - 1);
        for (int j = 0; j < secondTitle.size(); j++) {
            String secTitle = secondTitle.get(j);
            if (title.equals(secTitle)) { // 遍历所有标题，寻找到二级标题
                String content = "";
                int begin = allTitleIndex.get(len - 1);
                for (int k = begin + 1; k < nodes.size(); k++) {
                    Element node = nodes.get(k);
                    String text = node.text();
                    if (text.contains("词条图册") || text.contains("词条标签")) {
                        break;
                    }
                    content += "\n" + text;
                }
                secondContent.put(title, content.trim());
            }
        }
        return secondContent;
    }

    /**
     * 获取一级标题对应的内容
     *
     * @param doc
     * @return 一级标题列表及其对应内容列表的映射
     */
    public HashMap<String, String> getFirstContent(Document doc) {
        HashMap<String, String> firstContent = new HashMap<String, String>();
        LinkedList<String> firstTitle = getFirstTitle(doc);
        LinkedList<Element> nodes = getNodes(doc);

        // 寻找一级标题在节点链表的下标
        LinkedList<Integer> firstTitleIndex = getFirstTitleIndex(firstTitle, nodes);

        // 比较标题链表和对应的下标链表的大小是否相同，原则上是相同的，不相同说明网页存在问题等。。。
        int len = firstTitle.size();
        int indexLen = firstTitleIndex.size();
        if (len > indexLen) {
            len = indexLen;
        }

        // 获取每个一级标题的内容，为该标题与相邻标题下标之间的节点内容
        for (int i = 0; i < len - 1; i++) {
            String title = firstTitle.get(i);
            String content = "";
            int begin = firstTitleIndex.get(i);
            int end = firstTitleIndex.get(i + 1);
            for (int j = begin + 1; j < end; j++) {
                Element node = nodes.get(j);
                content += "\n" + node.text();
            }
            firstContent.put(title, content.trim());
        }
        // 一级标题最后一个标题为该下标到节点最后
        String title = firstTitle.get(len - 1);
        String content = "";
        int begin = firstTitleIndex.get(len - 1);
        for (int j = begin + 1; j < nodes.size(); j++) {
            Element node = nodes.get(j);
            String text = node.text();
            if (text.contains("词条图册") || text.contains("词条标签")) {
                break;
            }
            content += "\n" + text;
        }
        firstContent.put(title, content.trim());

        return firstContent;
    }

    /**
     * 寻找所有标题在节点链表的下标
     *
     * @param allTitle 所有标题列表
     * @param nodes    对象doc所有子节点列表
     * @return 所有标题index列表
     */
    public LinkedList<Integer> getAllTitleIndex(
            LinkedList<String> allTitle, LinkedList<Element> nodes) {
        LinkedList<Integer> allTitleIndex = new LinkedList<Integer>();
        // 寻找一级和二级标题在节点链表的下标
        for (int i = 0; i < allTitle.size(); i++) {
            String title = allTitle.get(i);
            for (int j = 0; j < nodes.size(); j++) {
                Element node = nodes.get(j);
                // 匹配到一级标题的下标
                Elements h2 = node.select("h2.title-text");
                if (h2.size() != 0) {
                    String level1 = h2.get(0).childNode(1).toString();
                    if (title.equals(level1)) {
                        allTitleIndex.add(j);
                    }
                }
                // 匹配到二级标题的下标
                Elements h3 = node.select("h3.title-text");
                if (h3.size() != 0) {
                    String level2 = h3.get(0).childNode(1).toString();
                    if (title.equals(level2)) {
                        allTitleIndex.add(j);
                    }
                }
            }
        }
        return allTitleIndex;
    }


    /**
     * 寻找一级标题在节点链表的下标
     *
     * @param firstTitle 一级标题列表
     * @param nodes      对象doc所有子节点列表
     * @return 一级标题index列表
     */
    public LinkedList<Integer> getFirstTitleIndex(
            LinkedList<String> firstTitle, LinkedList<Element> nodes) {
        LinkedList<Integer> firstTitleIndex = new LinkedList<Integer>();
        // 寻找一级标题在节点链表的下标
        for (int i = 0; i < firstTitle.size(); i++) {
            String title = firstTitle.get(i);
            for (int j = 0; j < nodes.size(); j++) {
                Element node = nodes.get(j);
                Elements h2 = node.select("h2.title-text");
                if (h2.size() != 0) {
                    String level1 = h2.get(0).childNode(1).toString();
                    if (title.equals(level1)) {// 匹配到一级标题的下标
                        firstTitleIndex.add(j);
                    }
                }
            }
        }
        return firstTitleIndex;
    }

    /**
     * 获取一级和二级标题
     *
     * @param doc
     * @return 一级标题和二级标题列表
     */
    public LinkedList<String> getAllTitle(Document doc) {
        LinkedList<String> allTitle = new LinkedList<>();
        Elements titles = doc.select("div[class^=para-title level-]");
        if (titles.size() != 0) {
            for (int i = 0; i < titles.size(); i++) {
                Elements h2title = titles.get(i).select("h2");
                Elements h3title = titles.get(i).select("h3");
                if (h2title.size() != 0) {
                    String level1 = h2title.get(0).childNode(1).toString();
                    allTitle.add(level1);
                } else if (h3title.size() != 0) {
                    String level2 = h3title.get(0).childNode(1).toString();
                    allTitle.add(level2);
                }
            }
        }
        return allTitle;
    }

    /**
     * 获取二级标题
     *
     * @param doc
     * @return 二级标题列表
     */
    public LinkedList<String> getSecondTitle(Document doc) {
        LinkedList<String> secondTitle = new LinkedList<String>();
        Elements titles = doc.select("div[class$=level-3]");
        if (titles.size() != 0) {
            for (int i = 0; i < titles.size(); i++) {
                String level2 = titles.get(i).select("h3").get(0).childNode(1)
                        .toString();
                secondTitle.add(level2);
            }
        }
        return secondTitle;
    }

    /**
     * 获取一级标题
     *
     * @param doc
     * @return 一级标题列表
     */
    public LinkedList<String> getFirstTitle(Document doc) {
        LinkedList<String> firstTitle = new LinkedList<String>();
        Elements titles = doc.select("div[class$=level-2]");
        if (titles.size() != 0) {
            for (int i = 0; i < titles.size(); i++) {
                String level1 = titles.get(i).select("h2").get(0).childNode(1).toString();
                firstTitle.add(level1);
            }
        }
        return firstTitle;
    }

    /**
     * 获取摘要信息
     *
     * @param doc
     * @return 摘要信息
     */
    public HashMap<String, String> getSummary(Document doc) {
        HashMap<String, String> map = new HashMap<>();
        String con = "";
        Elements content = doc.select("div[label-module=lemmaSummary]");// lemmaSummary：辅助总结
        if (content.size() != 0) {
            con = content.get(0).text();
        } else {
            logger.info("lemmaSummary is not existed...");
        }
        map.put("摘要", con.trim());
        return map;
    }

    /**
     * 解析发帖时间
     *
     * @param doc
     * @return 发帖时间
     */
    public String getPostTime(Document doc) {
        String time = "2016-01-01 00:00:00";
        try {
            Elements content = doc.select("div.side-content");
            if (content.size() != 0) {
                Elements timeItem = content.select("dd").select(
                        "span.j-modified-time");
                time = timeItem.get(0).text() + " 00:00:00";
            } else {
                logger.info("time don't exist...");
            }
        } catch (Exception e) {
            logger.info("time can't crawler...");
        }
        return time;
    }

    /**
     * 获取爬取时间
     *
     * @return 系统当前时间
     */
    public String getScratchTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String scratchTime = df.format(new Date());
        return scratchTime;
    }

    /**
     * 将html内容中的所有子节点写到链表中
     *
     * @param doc
     * @return 对象doc中所有子节点
     */
    public LinkedList<Element> getNodes(Document doc) {
        Element mainContent = doc.select("div.main-content").get(0);
        Elements children = mainContent.children();
        LinkedList<Element> list = new LinkedList<>();
        for (Element e : children) {
            list.offer(e);
        }
        return list;
    }

    /**
     * 得到doc的h1标题信息，错误页对应的h1一致
     *
     * @param doc
     * @return h1标题信息
     */
    public String getHead(Document doc) {
        String head = "";
        Elements a = doc.select("h1");
        if (a.size() != 0) {
            String h1 = a.get(0).text();
            head = h1;
        } else { // 没有h1标签，网页链接错误，设置head为错误页
            head = "百度百科错误页";
        }
        return head;
    }

    /**
     * 解析知识点词条term对应的url
     *
     * @param url
     * @return Document对象
     */
    public Document getDoc(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
//            doc = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla")
//                    .cookie("auth", "token").timeout(3000).post();
        } catch (Exception e) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e1) {
                logger.error("Jsoup get 方法失败：" + e);
            }
            logger.error("Jsoup post 方法失败：" + e);
        }
        return doc;
    }

    /**
     * 获取各级标题与分面的对应情况
     *
     * @param doc
     * @return 各级标题和一级标题的映射
     */
    public HashMap<String, String> getTitleRelation(Document doc) {
        LinkedList<String> indexs = new LinkedList<>();// 标题前面的下标
        LinkedList<String> facets = new LinkedList<>();// 各级标题的名字
        LinkedList<String> results = new LinkedList<>();// 二级标题对应到一级标题之后的标题
        HashMap<String, String> relation = new HashMap<>();

        // 获取标题
        Elements titles = doc.select("div[class^=catalog-list]").select("li");
        if (titles.size() != 0) {
            for (int i = 0; i < titles.size(); i++) {
                String index = titles.get(i).child(0).text();
                String text = titles.get(i).child(1).text();
                // Log.log(index + " " + text);
                indexs.add(index);
                facets.add(text);
                results.add(text);
            }

            // 将二级标题全部匹配到对应的一级标题
            for (int i = 0; i < indexs.size(); i++) {
                String index = indexs.get(i);
                if (index.equals("▪")) {
                    for (int j = i - 1; j >= 0; j--) { // 从二级标题往前搜索，遇到第一个下标不是"▪"的标题即是对应的一级标题
                        String indexCom = indexs.get(j);
                        if (!indexCom.equals("▪")) {
                            String facetOne = facets.get(j);
                            results.set(i, facetOne);
                            break;
                        }
                    }
                }
            }

            // 打印最新的标题信息，确定更新二级标题成功
            for (int i = 0; i < facets.size(); i++) {
                relation.put(facets.get(i), results.get(i));
            }

        } else {
            logger.info("该主题没有目录，不是目录结构，直接爬取 -->摘要<-- 信息");
        }

        return relation;
    }

    /**
     * 将所有标题映射为一级标题
     *
     * @param title    各级标题
     * @param relation 各级标题之间的映射关系
     * @return 各级标题title对应的一级标题
     */
    public String titleToFacet(String title, HashMap<String, String> relation) {
        String facetName = title;
        for (Map.Entry<String, String> entry : relation.entrySet()) {
            String tit = entry.getKey();
            String facet = entry.getValue();
            if (title.equals(tit)) {
                facetName = facet;
                break;
            }
        }
        return facetName;
    }

    /**
     * 解析知识点词条term某一分面对应的“百度图片”链接
     *
     * @param url term+facet搜索得到的百度图片链接
     * @return Document对象
     */
    public Document getDocImage(String url) {
        Document doc = null;

        WebDriver driver = new PhantomJSDriver();

        int i = 0;
        while (true) {
            try {
//				driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                driver.get(url);
                logger.info("try to crawler by phantomjs success....");
            } catch (Exception e) {
                logger.info("try to crawler by phantomjs again...");
                i++;
                driver.quit();
                driver = new PhantomJSDriver();
//				driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                if (i <= 3) {
                    continue;
                } else {
                    return null;
                }

            }
            break;
        }
        try {
            String html = driver.getPageSource();
            doc = Jsoup.parse(html);
            driver.quit();
        } catch (Exception e) {
            logger.info("Error at loading the page ...");
            driver.quit();
        }
        return doc;
    }

    /**
     * 解析图片网页，获取网页上前5张图片链接
     *
     * @param doc 知识点词条term的某一分面对应的doc对象
     * @return 该term分面下的前5张图片
     */
    public ArrayList<String> getFacetImage(Document doc) {
        ArrayList<String> ImageURLList = new ArrayList<>();
        Elements imgitems = doc.getElementsByClass("imgitem");
        int i = 0;
        for (Element imgitem : imgitems) {
            i++;
            String imglist = imgitem.attr("data-objurl");
            if (i < Config.imageCount + 1) {
                ImageURLList.add(imglist);
            }
        }
        return ImageURLList;
    }

}
