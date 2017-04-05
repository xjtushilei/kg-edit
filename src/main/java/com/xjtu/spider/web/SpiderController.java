package com.xjtu.spider.web;

import com.xjtu.common.Config;
import com.xjtu.common.domain.Error;
import com.xjtu.common.domain.Success;
import com.xjtu.dashboard.domain.ClassStatus;
import com.xjtu.dashboard.repository.ClassStatusRepository;
import com.xjtu.datainput.domain.Catalog;
import com.xjtu.datainput.domain.Relation;
import com.xjtu.datainput.domain.RelationCatalog;
import com.xjtu.datainput.domain.Term;
import com.xjtu.datainput.repository.CatalogRepository;
import com.xjtu.datainput.repository.RelationRepository;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.spider.domain.*;
import com.xjtu.spider.repository.ErrorTermRepository;
import com.xjtu.spider.repository.FacetRepository;
import com.xjtu.spider.repository.ImageRepository;
import com.xjtu.spider.repository.TextRepository;
import com.xjtu.spider.service.DeleteEmptyService;
import com.xjtu.spider.service.SpiderService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 爬虫的API
 * Created by yuanhao on 2017/3/16.
 */

@RestController
@RequestMapping("/spider")
public class SpiderController {

    // 引用其他类
    private static SpiderService spiderService = new SpiderService();
    private static DeleteEmptyService deleteEmptyService = new DeleteEmptyService();
    // 打印信息
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    // 数据库相关操作实例
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private TermRepository termRepository;
    @Autowired
    private ErrorTermRepository errorTermRepository;
    @Autowired
    private ClassStatusRepository classStatusRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private FacetRepository facetRepository;


    @RequestMapping(value = "/storeAllTermByClassID", method = RequestMethod.GET)
    @ApiOperation(value = "爬取课程", notes = "输入课程名，爬取该课程的所有文本和图片")
    public ResponseEntity storeAllTermByClassID(
            @RequestParam(value = "ClassID", defaultValue = "1") String classID
    ) {
        List<Term> termList = new ArrayList<>();
        try {
            // 获取所有知识点
            termList = termRepository.findByClassID(classID);
            for (int i = 0; i < termList.size(); i++) {
                Term term = termList.get(i);

                // 获取每个知识点的文本
                logger.info("爬取知识点文本开始。。。" + term.getTermName());
                storeSingleTermText(term.getTermID(), term.getTermName());
                logger.info("爬取知识点文本结束。。。" + term.getTermName());

                // 获取每个知识点的图片
                logger.info("爬取知识点图片开始。。。" + term.getTermName());
                storeSingleTermImage(term.getTermID(), term.getTermName());
                logger.info("爬取知识点图片结束。。。" + term.getTermName());

                // 获取每个知识点的分面
                logger.info("爬取知识点分面开始。。。" + term.getTermName());
                storeSingleTermFacet(term.getTermID(), term.getTermName());
                logger.info("爬取知识点分面结束。。。" + term.getTermName());
            }

            // 读取图片数据表，将每个图片的内容都成对应的API形式保存到对应字段。
            logger.info("设置知识点图片表中所有图片的API开始。。。");
            getImageAllApi();
            logger.info("爬取知识点图片表中所有图片的API结束。。。");

            return ResponseEntity.status(HttpStatus.OK).body(new Success("课程ID为：" + classID + "的课程已经处理完毕。。。"));
        } catch (Exception e) {
            logger.error("获取课程下的知识点失败。。。", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("课程ID为：" + classID + "的课程处理失败。。。"));
        }

    }


    //    @RequestMapping(value = "/storeSingleTermText", method = RequestMethod.GET)
//    @ApiOperation(value = "存储单个知识点的文本", notes = "输入单个知识点，爬取单个知识点的文本")
    public ResponseEntity storeSingleTermText(
            @RequestParam(value = "TermID", defaultValue = "1") Long termID,
            @RequestParam(value = "TermName", defaultValue = "双曲函数") String termName
    ) {
        ResponseEntity responseEntity = null;

        // 判断知识点是否可以爬取
        Boolean exist = spiderService.judgeFragmentExist(termName);
        // 存储文本碎片
        if (exist) {
            // 判断知识点是否已经存在，防止重复爬取
            Boolean alreadyCrawler = judgeTermExistText(termID);
            if (!alreadyCrawler) {
                // 爬取知识点文本
                List<Text> textList = spiderService.getSingleTerm(termName, termID); // 爬虫获取所有分面数据
                List<Text> beauTextList = new ArrayList<>();

                for (int i = 0; i < textList.size(); i++) {
                    Text text = textList.get(i);
                    try {
//                        logger.info("碎片内容为：" + text.getFragmentContent());
//                        logger.info("textRepository: " + textRepository);
                        if (!text.getFragmentContent().equals("")) {
                            textRepository.save(text);
                            beauTextList.add(text);
                        } else {
                            logger.info("文本内容为'',因此不进行存储。。。");
                        }
                    } catch (Exception e) {
                        logger.error(e + "");
                        logger.error("文本内容为null,因此不进行存储。。。" + e);
                    }

                }
                logger.info("文本个数 : " + beauTextList.size());

//                textRepository.save(beauTextList); // 可以save一堆Text对象，牛逼
//                beauTextList.iterator().forEachRemaining(t->{textRepository.save(t);}); // jdk8新特性：拉姆达表达式
                responseEntity = ResponseEntity.status(HttpStatus.OK).body(beauTextList);
                logger.info(termName + " 的文本爬取结束。。。");
            } else {
                // 知识点文本已经存在，无需再次爬取
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(" 的文本已经爬取，无需再次爬取。。。"));
                logger.info(termName + " 的文本已经爬取，无需再次爬取。。。");
            }
        } else {
            // 知识点不存在百度百科页面，将其记录到错误知识点表格中
            logger.info(termName + " 不存在百度百科页面，无文本。。。");
            logger.info(termName + " 知识点是错误知识点，将其加到错误知识点表格中");
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(" 不存在百度百科页面，无文本，错误知识点。。。"));

            // 判断知识点是否已经存在错误知识点表格中，防止重复存储
            Boolean existErrorTerm = judgeErrorTermExist(termID);
            if (!existErrorTerm) {
                // 存储错误知识点，包括其所属章节信息

                // 1. 从datainput_relation表中确定该term的CatalogID（可能会有多个）
                Set<Long> catalogIDList = new TreeSet<>();
                List<Relation> relationList = relationRepository.findByTermID(termID);
                for (int i = 0; i < relationList.size(); i++) {
                    Relation relation = relationList.get(i);
                    catalogIDList.add(relation.getCatalogID());
                }

                // 2. 根据CatalogID从datainput_catalog表中读取所有爬取失败节点的章节信息
                Set<Catalog> catalogSet = new HashSet<>(); // 保存所有章节信息
                for (Long catalogID : catalogIDList) {
                    Catalog catalog = new Catalog();
                    try {
                        catalog = catalogRepository.findByCatalogID(catalogID);
                        catalogSet.add(catalog);
                    } catch (Exception e) {
                        logger.error("查询章节信息失败。。。", e);
                    }
                }

                // 3. 将所有错误章节的term及章节信息保存到spider_errorterm表格中（存储之前确认是否已经存在于数据库）
                for (Catalog catalog : catalogSet) {
                    ErrorTerm errorTerm = new ErrorTerm(termID, termName, catalog.getCatalogID(), catalog.getParentChapterID(),
                            catalog.getParentChapterName(), catalog.getChapterID(), catalog.getChapterName(),
                            catalog.getChildrenChapterID(), catalog.getChildrenChapterName());
                    errorTermRepository.save(errorTerm);
                }

            } else {
                logger.info("该错误知识点已经存在于错误知识点表格中，无需再次存储。。。");
            }


        }

        return responseEntity;
    }


    //    @RequestMapping(value = "/storeSingleTermImage", method = RequestMethod.GET)
//    @ApiOperation(value = "存储单个知识点的图片", notes = "输入单个知识点，爬取单个知识点的图片")
    public ResponseEntity storeSingleTermImage(
            @RequestParam(value = "TermID", defaultValue = "2") Long termID,
            @RequestParam(value = "TermName", defaultValue = "你好") String termName  //   人不能太帅
    ) {
        ResponseEntity responseEntity = null;

        // 判断知识点是否可以爬取
        Boolean exist = spiderService.judgeFragmentExist(termName);
        // 存储文本碎片
        if (exist) {
            // 判断知识点是否已经存在，防止重复爬取
            Boolean alreadyCrawler = judgeTermExistImage(termID);
            if (!alreadyCrawler) {
                // 爬取知识点图片
                List<Image> imageList = spiderService.getSingleTermFacetImages(termName, termID); // 爬虫获取所有分面数据

                // 图片表格设计为imageContent内容不为空，因此存储内容为空的图片时会报错，这个时候捕获异常然后不进行处理即可
                List<Image> beauImageList = new ArrayList<>();
                for (int i = 0; i < imageList.size(); i++) {
                    Image image = imageList.get(i);
                    try {
                        imageRepository.save(image);
                        beauImageList.add(image);
                    } catch (Exception e) {
                        logger.error("图片内容为null，不进行存储。。。" + e);
                    }
                }
                logger.info("图片个数 : " + beauImageList.size());

                responseEntity = ResponseEntity.status(HttpStatus.OK).body(beauImageList);
                logger.info(termName + " 的图片爬取结束。。。");
            } else {
                // 知识点文本已经存在，无需再次爬取
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(" 的图片已经爬取，无需再次爬取。。。"));
                logger.info(termName + " 的图片已经爬取，无需再次爬取。。。");
            }
        } else {
            // 知识点不存在百度百科页面，将其记录到错误知识点表格中
            logger.info(termName + " 不存在百度百科页面，无图片。。。");
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(" 不存在百度百科页面，无图片，错误知识点。。。"));
        }

        return responseEntity;
    }


    //    @RequestMapping(value = "/storeSingleTermFacet", method = RequestMethod.GET)
//    @ApiOperation(value = "存储单个知识点的分面", notes = "输入单个知识点，爬取单个知识点的分面")
    public ResponseEntity storeSingleTermFacet(
            @RequestParam(value = "TermID", defaultValue = "1") Long termID,
            @RequestParam(value = "TermName", defaultValue = "双曲函数") String termName
    ) {
        ResponseEntity responseEntity = null;

        // 判断知识点是否可以爬取
        Boolean exist = spiderService.judgeFragmentExist(termName);
        // 存储文本碎片
        if (exist) {
            // 判断知识点是否已经存在，防止重复爬取
            Boolean alreadyCrawler = judgeTermExistFacet(termID);
            if (!alreadyCrawler) {

                // 爬取知识点分面
                List<Text> facetList = spiderService.getSingleTerm(termName, termID); // 爬虫获取所有分面数据
                List<Text> beauFacetList = deleteEmptyService.delEmptyContentSingleTerm(facetList); // 去除内容为空的分面数据
                logger.info("分面个数 : " + beauFacetList.size());

                // 获取一个主题的所有分面信息集合，无重复Set集合
                Set<String> facetSet = new HashSet<String>();
                for (int i = 0; i < beauFacetList.size(); i++) {
                    Text f = beauFacetList.get(i);
                    facetSet.add(f.getFacetName());
                }

                // 存储分面信息
                List<Facet> facetList1 = new ArrayList<>();
                for (String facetName : facetSet) {
                    Facet facet = new Facet(termID, termName, facetName, "");
                    facetList1.add(facet);
                    logger.info(facet.toString());
                    try {
                    facetRepository.save(facet); // 持久化到数据库
                    } catch (Exception e) {
                        logger.error("存储分面出错。。。" + e);
                    }
                }

                responseEntity = ResponseEntity.status(HttpStatus.OK).body(facetList1);
                logger.info(termName + " 的分面爬取结束。。。");
            } else {
                // 知识点文本已经存在，无需再次爬取
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(" 的分面已经爬取，无需再次爬取。。。"));
                logger.info(termName + " 的分面已经爬取，无需再次爬取。。。");
            }
        } else {
            // 知识点不存在百度百科页面，将其记录到错误知识点表格中
            logger.info(termName + " 不存在百度百科页面，无分面。。。");
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(" 不存在百度百科页面，无分面，错误知识点。。。"));
        }

        return responseEntity;
    }


    //    @RequestMapping(value = "/getImageAllApi", method = RequestMethod.GET)
//    @ApiOperation(value = "处理图片API", notes = "读取图片内容为api格式，并将其写到图片数据表中")
    public ResponseEntity getImageAllApi() {
        ResponseEntity responseEntity = null;
        List<Image> imageList = new ArrayList<>();
        try {
            // 得到所有图片
            imageList = imageRepository.findAll();

            // 更新图片的API字段
            for (int i = 0; i < imageList.size(); i++) {
                Long imageID = imageList.get(i).getImageID();
                String api = Config.project + "/" + Config.imageAPICata + "/getImage?imageID=" + imageID;
                imageRepository.updateByImageID(api, imageID); // 根据imageID更新imageAPI
            }

            responseEntity = ResponseEntity.status(HttpStatus.OK).body(imageList);
        } catch (Exception e) {
            logger.error("出现错误：" + e);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("处理图片API操作失败。。。"));
        }
        return responseEntity;
    }


    @RequestMapping(value = "/getImage", method = RequestMethod.GET)
    @ApiOperation(value = "访问图片的API", notes = "输入图片ID，返回对应图片的API")
    public ResponseEntity getImage(
            @RequestParam(value = "imageID", defaultValue = "1") Long imageID
    ) {
        ResponseEntity responseEntity = null;
        Image image = new Image();
        try {
            image = imageRepository.findByImageID(imageID);
            String fileName = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1); // 图片文件名字取链接最后一部分
            Object imageCount = image.getImageContent(); // 图片二进制流数据
            responseEntity = ResponseEntity.status(HttpStatus.OK).header("Content-disposition", "attachment; "
                    + "filename=" + fileName).contentType(MediaType.APPLICATION_OCTET_STREAM).body(imageCount);
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("获 取图片操作失败。。。"));
        }
        return responseEntity;
    }


    @RequestMapping(value = "/getClassStatus", method = RequestMethod.GET)
    @ApiOperation(value = "读取所有门课程的爬取进度", notes = "读取系统进度表格，返回所有课程现在的爬取情况")
    public ResponseEntity getClassStatus(@RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID) {
        ResponseEntity responseEntity = null;
        List<ClassStatus> classStatusList = new ArrayList<>();
        try {
            // 找到所有正在运行爬虫的课程
            classStatusList = classStatusRepository.findByClassid(ClassID);
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("读取系统进度表格操作失败。。。"));
        }

        List<SpiderStatus> spiderStatusList = new ArrayList<>();
        // 判断是有课程正在爬取
        if (classStatusList.size() != 0) {
            // 循环遍历每门课程，得到每门课程的爬取状态，包括课程名、课程term总数、课程出错term数和已处理term数
            for (int i = 0; i < classStatusList.size(); i++) {

                // 正在爬取的课程对象
                ClassStatus classStatus = classStatusList.get(i);

                // 查询正在爬取的课程名
                String className = classStatus.getClassname();

                // 查询term表得到本门课程知识点总数（不变）
                List<Term> termList = termRepository.findByClassID(classStatus.getClassid()); // 该课程所有知识点
                int termSum = termList.size();

                // 查询errorTerm表得到目前错误知识点的数目
                int errorTermNum = 0;
                for (int j = 0; j < termList.size(); j++) {
                    Long termID = termList.get(j).getTermID(); // 该课程的每个知识点
                    int singleTermCount = errorTermRepository.findByTermID(termID).size(); // 错误表中存在的知识点错误记录数目
                    errorTermNum += singleTermCount;
                }

                // 查询image表得到目前爬取知识点的个数
                int alreadyTermNum = 0;
                for (int j = 0; j < termList.size(); j++) {
                    Long termID = termList.get(j).getTermID();
                    int size = imageRepository.findByTermID(termID).size(); // 图片表中该知识点的图片集合
                    if (size != 0) { // 数目不为0说明该词条已经爬取了
                        alreadyTermNum++;
                    }
                }
                alreadyTermNum += errorTermNum; //  已经处理的知识点个数，包括爬取的和未能爬取的

                /**
                 * 有可能之前有两个主题可以爬到数据，但是老师把他们删除了（不是删除错误词表中的词），然后又进行重新爬取
                 * 爬虫这个时候因为删除后的主题集合的主题肯定都在现有的图片表中，所以不会删除表格重新爬取
                 * 这样会导致图片表格中的主题数目比现在的多
                 * 因此，加一个判断语句：alreadyTermNum一定要小于等于termSum，这样爬去进度不会超过100%
                 */
                if (alreadyTermNum > termSum) {
                    alreadyTermNum = termSum;
                }

                // 保存每个正在爬取的课程的状态，返回集合
                SpiderStatus spiderStatus = new SpiderStatus(className, termSum, errorTermNum, alreadyTermNum);
                spiderStatusList.add(spiderStatus);

            }
            logger.info("获取所有正在爬取课程的状态结束。。。");
            // 循环结束，设置返回结果为所有正在爬取课程的状态
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(spiderStatusList);
        } else {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("没有课程正在执行爬虫。。。"));
        }
        return responseEntity;
    }


    @RequestMapping(value = "/updateErrorTerm", method = RequestMethod.GET)
    @ApiOperation(value = "修改错误表中的term", notes = "当在spider页面进行修改无法爬取的知识点的时候，修改error表")
    public ResponseEntity updateErrorTerm(
            @RequestParam(value = "termName", defaultValue = "你好") String oldTermName,
            @RequestParam(value = "newTermName", defaultValue = "你不好") String newTermName
    ) {
        ResponseEntity responseEntity = null;
        try {
            errorTermRepository.updateErrorTerm(newTermName, oldTermName);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new Success("修改错误表中的term操作成功。。。"));
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("修改错误表中的term操作失败。。。"));
        }
        return responseEntity;
    }


    @RequestMapping(value = "/deleteErrorTerm", method = RequestMethod.GET)
    @ApiOperation(value = "删除错误表中的term", notes = "当在spider页面进行修改无法爬取的知识点的时候，删除error表中的记录")
    public ResponseEntity deleteErrorTermInfo(
            @RequestParam(value = "TermID", defaultValue = "1") Long termID
    ) {
        ResponseEntity responseEntity = null;
        try {
            errorTermRepository.deleteErrorTerm(termID);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new Success("删除错误表中的term操作成功。。。"));
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除错误表中的term操作失败。。。"));
        }
        return responseEntity;
    }


    @RequestMapping(value = "/getErrorTermInfo", method = RequestMethod.GET)
    @ApiOperation(value = "读取错误表中的term", notes = "读取课程爬取结束后的错误term信息")
    public ResponseEntity getErrorTerm(@RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID) {
        ResponseEntity responseEntity = null;
        List<RelationCatalog> relationCatalog = new ArrayList<>();
        try {
            relationCatalog = errorTermRepository.findErrorByClassID(ClassID);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(relationCatalog);
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("读取错误表中的term操作失败。。。"));
        }
        return responseEntity;
    }


    /**
     * 判断知识点是否已经爬取到文本表格中
     *
     * @param termID 知识点ID
     * @return
     */
    public Boolean judgeTermExistText(Long termID) {
        List<Text> textList = new ArrayList<>();
        try {
            textList = textRepository.findByTermID(termID);
            if (textList.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 判断知识点是否已经爬取到图片表格中
     *
     * @param termID 知识点ID
     * @return
     */
    public Boolean judgeTermExistImage(Long termID) {
        List<Image> imageList = new ArrayList<>();
        try {
            imageList = imageRepository.findByTermID(termID);
            if (imageList.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断知识点是否已经爬取到分面表格中
     *
     * @param termID 知识点ID
     * @return
     */
    public Boolean judgeTermExistFacet(Long termID) {
        List<Facet> facetList = new ArrayList<>();
        try {
            facetList = facetRepository.findByTermID(termID);
            if (facetList.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断知识点是否已经在错误知识点表格中
     *
     * @param termID 知识点ID
     * @return
     */
    public Boolean judgeErrorTermExist(Long termID) {
        List<ErrorTerm> errorErrorTermList = new ArrayList<>();
        try {
            errorErrorTermList = errorTermRepository.findByTermID(termID);
            if (errorErrorTermList.size() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


}
