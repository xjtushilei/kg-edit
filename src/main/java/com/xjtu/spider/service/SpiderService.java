package com.xjtu.spider.service;

import com.xjtu.spider.domain.Image;
import com.xjtu.spider.domain.Text;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 爬虫程序调用的接口，输入需要爬取的知识点和ID，返回文本Text集合和图片Image集合
 * <p>
 * 爬取单个词条的文本和图片
 * 1. 爬取单个词条的文本：getSingleTerm
 * 2. 爬取单个词条的图片：getSingleTermFacetImages
 * 3. 判断词条是否可以爬取：judgeFragmentExist
 * <p>
 * Created by yuanhao on 2017/3/16.
 */
public class SpiderService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private ParseService parseFragmentService = new ParseService();
    private FacetService facetAlgoService = new FacetService();


    public static void main(String[] args) {
        SpiderService spiderService = new SpiderService();

        // 知识点存在与否判断
        boolean exist = spiderService.judgeFragmentExist("你好");
        System.out.println(exist);

        // 文本爬取测试：输入知识点和ID，返回List<Text>
        List<Text> textList = spiderService.getSingleTerm("你好", 1L);
        for (Text text : textList) {
            System.out.println(text.toString());
        }

        // 图片爬取测试：输入知识点和ID，返回List<Image>
//        List<Image> imageList = spiderService.getSingleTermFacetImages("邻域", 10L);
//        for (Image image : imageList) {
//            System.out.println(image.toString());
//        }
    }

    /**
     * 获取单个term的所有碎片
     *
     * @param term   知识点词条
     * @param termID 知识点词条的唯一ID
     * @return 碎片集合
     */
    public List<Text> getSingleTerm(String term, Long termID) {

        // 在百度图片上的搜索名字，中文在链接中进行UTF-8编码转换
        String termNew = term;
        try {
            termNew = URLEncoder.encode(termNew, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String url = "http://baike.baidu.com/item/" + termNew;
        List<Text> list = new ArrayList<>();

        // 在线解析
        Document doc = null;
        try {
            doc = parseFragmentService.getDoc(url);
        } catch (Exception e) {
            logger.info("SpiderService --> ParseService --> getDoc error...");
        }
        // 判断doc是否可以得到，即爬虫是否可以访问百度网页
        if (doc != null) {
            logger.info("开始爬取知识点：" + term);
            HashMap<String, String> map = parseFragmentService.getAllContent(doc, true, true); // 获取这个主题下的所有分面文本
            HashMap<String, String> relation = parseFragmentService.getTitleRelation(doc); // 各级标题转化为分面

            // 保存所有数据
            logger.info("-----------------------------------------------------");
            for (String facet : map.keySet()) {
                // 保存每个Fragment对象
                String fragmentContent = map.get(facet);
                String fragmentUrl = url;
                String fragmentPostTime = parseFragmentService.getPostTime(doc);
                String fragmentScratchTime = parseFragmentService.getScratchTime();
                String termName = term;
                String facetName = parseFragmentService.titleToFacet(facet, relation); // 分面名全部设为一级标题（新标准）
                facetName = facetAlgoService.getfacet(facetName); // 采用分面算法进行处理
                Text text = new Text(fragmentContent, fragmentUrl, fragmentPostTime, fragmentScratchTime, termID, termName, facetName);
//                logger.info(text.toString());
                list.add(text);
            }
        } else {
            logger.error("can't get page doc, the ParseService --> getDoc function error...");
        }
        return list;
    }

    /**
     * 获取单个term所有分面对应的图片数据
     *
     * @param term   知识点词条
     * @param termID 知识点词条的唯一ID
     * @return 分面图片集合
     */
    public List<Image> getSingleTermFacetImages(String term, Long termID) {

        // 在百度图片上的搜索名字，中文在链接中进行UTF-8编码转换
        String termNew = term;
        try {
            termNew = URLEncoder.encode(termNew, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // 解析term百科页面，得到分面列表
        String urlBaike = "http://baike.baidu.com/item/" + termNew;
        List<Image> list = new ArrayList<>();
        Document docBaike = parseFragmentService.getDoc(urlBaike);
        LinkedList<String> facetList = parseFragmentService.getFirstTitle(docBaike); // 按照一级标题存储
        facetList.add("摘要");
        logger.info(term + "分面个数为：" + facetList.size());
        for (int i = 0; i < facetList.size(); i++) {
            String facet = facetList.get(i);
            facet = facetAlgoService.getfacet(facet); // 采用分面算法进行处理
            logger.info("-------------正在处理主题分面的图片信息----------------");
            logger.info(term + "_" + facet);
            String facetSearch = term + " " + facet; // 在百度图片上的搜索名字
            try {
                facetSearch = URLEncoder.encode(facetSearch, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String facetImageUrl = "http://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592"
                    + "&cl=2&lm=-1&st=-1&fm=index&fr=&sf=1&fmq=&pv=&ic=0&nc=1&z=&se=1"
                    + "&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word="
                    + facetSearch;
            Document docFacetImage = parseFragmentService.getDocImage(facetImageUrl); // 解析
            ArrayList<String> facetImageList = parseFragmentService.getFacetImage(docFacetImage); // 分面下的前几张图片的链接
            for (int j = 0; j < facetImageList.size(); j++) {
                logger.info("图片链接：" + facetImageList.get(j));
                // 内容为InputStream流，对应数据库字段属性为Blob
                byte[] imageContent = ImageService.getImageFromNetByUrl(facetImageList.get(j)); // 下载图片url为输入流
                String imageUrl = facetImageList.get(j);
                String imageScratchTime = parseFragmentService.getScratchTime();
                String termName = term;
                String facetName = facet;
                String imageAPI = null;
                Image fragment = new Image(imageContent, imageUrl, imageScratchTime, termID, termName, facetName, imageAPI);
                list.add(fragment);
            }
        }
        return list;
    }

    /**
     * 判断一个Term是否存在对应的百度百科词条链接
     *
     * @param term 知识点词条
     * @Return 判断词条是否可以爬取的布尔值
     */
    public boolean judgeFragmentExist(String term) {
        logger.info("begin judge term");
        boolean flag = true;
        if (!term.equals("")) {
            String termUrl = term;
            // 处理中文词条到链接中为UTF-8格式
            try {
                termUrl = new String(java.net.URLEncoder.encode(term, "utf-8").getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                String url = "http://baike.baidu.com/item/" + termUrl;
                Document doc = parseFragmentService.getDoc(url);
                String head = parseFragmentService.getHead(doc); // 得到网页头信息
                if (head.equals("百度百科错误页")) { // 没有获取到对应文档
                    flag = false;
                }
            } catch (Exception e) {
                logger.error("" + e);
            }


        } else { // term不存在
            flag = false;
        }
        return flag;
    }

}
