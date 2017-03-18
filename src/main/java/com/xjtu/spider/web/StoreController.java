package com.xjtu.spider.web;

import com.xjtu.common.Config;
import com.xjtu.common.domain.Error;
import com.xjtu.datainput.domain.Catalog;
import com.xjtu.datainput.domain.Relation;
import com.xjtu.datainput.repository.CatalogRepository;
import com.xjtu.datainput.repository.RelationRepository;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.spider.domain.ErrorTerm;
import com.xjtu.spider.domain.Facet;
import com.xjtu.spider.domain.Image;
import com.xjtu.spider.domain.Text;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by yuanhao on 2017/3/16.
 */

@RestController
@RequestMapping("/store")
public class StoreController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    // 引用其他类
    private static SpiderService spiderService = new SpiderService();
    private static DeleteEmptyService deleteEmptyService = new DeleteEmptyService();

    public static void main(String[] args) {
        StoreController storeController = new StoreController();
        storeController.storeSingleTermText(1L, "双曲函数");
    }

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
    private RelationRepository relationRepository;
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private FacetRepository facetRepository;


//    @RequestMapping(value = "/storeTerm", method = RequestMethod.GET)
//    @ApiOperation(value = "存储知识点", notes = "")
//    public Term storeTerm(
//            @RequestParam(value = "TermName", defaultValue = "双曲函数") String termName,
//            @RequestParam(value = "ClassID", defaultValue = "1") String classID,
//            @RequestParam(value = "ClassName", defaultValue = "测试课程") String className,
//            @RequestParam(value = "Note", defaultValue = "test") String note
//    ) {
//        Date lastModifyTime = new Date();
//        Term term = new Term(termName, classID, className, note, lastModifyTime);
//        try {
//            term = termRepository.save(term);
//        } catch (Exception e) {
//            // TODO: handle exception
//            logger.error("getlist failed, ", e);
//        }
//        return term;
//    }

    @RequestMapping(value = "/storeSingleTermText", method = RequestMethod.GET)
    @ApiOperation(value = "存储单个知识点的文本", notes = "输入单个知识点，爬取单个知识点的文本")
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
                        if (!text.getFragmentContent().equals("")) {
                            textRepository.save(text);
                        } else {
                            logger.info("文本内容为'',因此不进行存储。。。");
                        }
                    } catch (Exception e) {
                        logger.info("文本内容为null,因此不进行存储。。。");
                    }

                }

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


    @RequestMapping(value = "/storeSingleTermImage", method = RequestMethod.GET)
    @ApiOperation(value = "存储单个知识点的图片", notes = "输入单个知识点，爬取单个知识点的图片")
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
                        logger.info("图片内容为null，不进行存储。。。");
                    }

                }

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


    @RequestMapping(value = "/storeSingleTermFacet", method = RequestMethod.GET)
    @ApiOperation(value = "存储单个知识点的分面", notes = "输入单个知识点，爬取单个知识点的分面")
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
            Boolean alreadyCrawler = judgeTermExistImage(termID);
            if (!alreadyCrawler) {
                // 爬取知识点分面
                List<Text> facetList = spiderService.getSingleTerm(termName, termID); // 爬虫获取所有分面数据
                List<Text> beauFacetList = deleteEmptyService.delEmptyContentSingleTerm(facetList); // 去除内容为空的分面数据
                logger.info("size : " + facetList.size());
                logger.info("size : " + beauFacetList.size());
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
                    facetRepository.save(facet); // 持久化到数据库
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

//    @RequestMapping(value = "/emptyImage", method = RequestMethod.GET)
//    @ApiOperation(value = "删除内容为空的图片", notes = "查询图片表，删除内容为空的图片")
//    public ResponseEntity emptyImage() {
//        ResponseEntity responseEntity = null;
//        try {
//            byte[] imageContent = null;
//            imageRepository.deleteEmpty(imageContent); // 删除内容为空的元素
//            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new Success("删除空图片操作成功。。。"));
//        } catch (Exception e) {
//            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除空图片操作失败。。。"));
//        }
//        return responseEntity;
//    }
//
//    @RequestMapping(value = "/emptyText", method = RequestMethod.GET)
//    @ApiOperation(value = "删除内容为空的文本", notes = "查询文本表，删除内容为空的文本")
//    public ResponseEntity emptyText() {
//        ResponseEntity responseEntity = null;
//        try {
//            textRepository.deleteEmpty(""); // 删除内容为空的元素
//            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new Success("删除空文本操作成功。。。"));
//        } catch (Exception e) {
//            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除空文本操作失败。。。"));
//        }
//        return responseEntity;
//    }


    @RequestMapping(value = "/getImageAllApi", method = RequestMethod.GET)
    @ApiOperation(value = "处理图片API", notes = "读取图片内容为api格式，并将其写到图片数据表中")
    public ResponseEntity getImageAllApi() {
        ResponseEntity responseEntity = null;
        List<Image> imageList = new ArrayList<>();
        try {
            // 得到所有图片
            imageList = imageRepository.findAll();

            // 更新图片的API字段
            for (int i = 0; i < imageList.size(); i++) {
                Long imageID = imageList.get(i).getImageID();
                String api = "http://" + Config.server + "/" + Config.project + "/" + Config.imageAPICata + "/getImage?imageID=" + imageID;
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
            @RequestParam(value = "ImageID", defaultValue = "1") Long imageID
    ) {
        ResponseEntity responseEntity = null;
        Image image = new Image();
        try {
            image = imageRepository.findByImageID(imageID);
            String fileName = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1); // 图片文件名字取链接最后一部分
            Object imageCount = image.getImageContent(); // 图片二进制流数据
            responseEntity = ResponseEntity.status(HttpStatus.OK).header("Content-disposition", "attachment; "
                    + "filename=" + fileName).body(imageCount);
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("获取图片操作失败。。。"));
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
        Boolean exist = false;
        List<Text> textList = new ArrayList<>();
        try {
            textList = textRepository.findByTermID(termID);
            if (textList.size() != 0) {
                exist = true;
            }
        } catch (Exception e) {
            logger.error("获取文本表中的知识点失败。。。", e);
        }
        return exist;
    }

    /**
     * 判断知识点是否已经爬取到图片表格中
     *
     * @param termID 知识点ID
     * @return
     */
    public Boolean judgeTermExistImage(Long termID) {
        Boolean exist = false;
        List<Image> imageList = new ArrayList<>();
        try {
            imageList = imageRepository.findByTermID(termID);
            if (imageList.size() != 0) {
                exist = true;
            }
        } catch (Exception e) {
            logger.error("获取图片表的知识点失败。。。", e);
        }
        return exist;
    }

    /**
     * 判断知识点是否已经在错误知识点表格中
     *
     * @param termID 知识点ID
     * @return
     */
    public Boolean judgeErrorTermExist(Long termID) {
        Boolean exist = false;
        List<ErrorTerm> errorErrorTermList = new ArrayList<>();
        try {
            errorErrorTermList = errorTermRepository.findByTermID(termID);
            if (errorErrorTermList.size() != 0) {
                exist = true;
            }
        } catch (Exception e) {
            logger.error("获取课程下的知识点失败。。。", e);
        }
        return exist;
    }


}
