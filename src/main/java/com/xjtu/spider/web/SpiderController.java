package com.xjtu.spider.web;

import com.xjtu.common.domain.Error;
import com.xjtu.common.domain.Success;
import com.xjtu.datainput.domain.Term;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.spider.domain.ErrorTerm;
import com.xjtu.spider.repository.ErrorTermRepository;
import com.xjtu.spider.repository.ImageRepository;
import com.xjtu.spider.repository.TextRepository;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫的API
 * Created by yuanhao on 2017/3/16.
 */

@RestController
@RequestMapping("/spider")
public class SpiderController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private StoreController storeController = new StoreController();

    @Autowired
    private TextRepository textRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private TermRepository termRepository;
    @Autowired
    private ErrorTermRepository errorTermRepository;

    @RequestMapping(value = "/storeAllTermByClassID", method = RequestMethod.GET)
    @ApiOperation(value = "爬取课程", notes = "输入课程名，爬取该课程的所有文本和图片")
    public ResponseEntity storeAllTermByClassID(
            @RequestParam(value = "ClassID", defaultValue = "测试课程") String classID
    ) {
        List<Term> termList = new ArrayList<>();
        try {
            // 获取所有知识点
            termList = termRepository.findByClassID(classID);
            for (int i = 0; i < termList.size(); i++) {
                Term term = termList.get(i);
                // 获取每个知识点的文本
                logger.info("爬取知识点文本开始。。。" + term.getTermName());
                storeController.storeSingleTermText(term.getTermID(), term.getTermName());
                logger.info("爬取知识点文本结束。。。" + term.getTermName());
                // 获取每个知识点的图片
                logger.info("爬取知识点图片开始。。。" + term.getTermName());
                storeController.storeSingleTermImage(term.getTermID(), term.getTermName());
                logger.info("爬取知识点图片结束。。。" + term.getTermName());
                // 获取每个知识点的分面
                logger.info("爬取知识点分面开始。。。" + term.getTermName());
                storeController.storeSingleTermFacet(term.getTermID(), term.getTermName());
                logger.info("爬取知识点分面结束。。。" + term.getTermName());
            }

//            // 删除空文本和空图片
//            storeController.emptyText();
//            storeController.emptyImage();

            // 读取图片数据表，将每个图片的内容都成对应的API形式保存到对应字段。
            logger.info("设置知识点图片表中所有图片的API开始。。。");
            storeController.getImageAllApi();
            logger.info("爬取知识点图片表中所有图片的API结束。。。");


            return ResponseEntity.status(HttpStatus.OK).body(new Success("课程ID为：" + classID + "的课程已经处理完毕。。。"));
        } catch (Exception e) {
            logger.error("获取课程下的知识点失败。。。", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("课程ID为：" + classID + "的课程处理失败。。。"));
        }

    }


    @RequestMapping(value = "/updateErrorTerm", method = RequestMethod.GET)
    @ApiOperation(value = "修改错误表中的term", notes = "当在spider页面进行修改无法爬取的知识点的时候，修改error表")
    public ResponseEntity updateErrorTerm(
            @RequestParam(value = "OldTermName", defaultValue = "你好") String oldTermName,
            @RequestParam(value = "NewTermName", defaultValue = "你不好") String newTermName
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
            @RequestParam(value = "TermName", defaultValue = "你好") String termName
    ) {
        ResponseEntity responseEntity = null;
        try {
            errorTermRepository.deleteErrorTerm(termName);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new Success("删除错误表中的term操作成功。。。"));
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除错误表中的term操作失败。。。"));
        }
        return responseEntity;
    }

    @RequestMapping(value = "/getErrorTermInfo", method = RequestMethod.GET)
    @ApiOperation(value = "读取错误表中的term", notes = "读取课程爬取结束后的错误term信息")
    public ResponseEntity getErrorTerm() {
        ResponseEntity responseEntity = null;
        List<ErrorTerm> errorTermList = new ArrayList<>();
        try {
            errorTermList = errorTermRepository.findAll();
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(errorTermList);
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("读取错误表中的term操作失败。。。"));
        }
        return responseEntity;
    }

//    @RequestMapping(value = "/getClassStatus", method = RequestMethod.GET)
//    @ApiOperation(value = "读取所有门课程的爬取进度", notes = "读取系统进度表格，返回所有课程现在的爬取情况")
//    public ResponseEntity getClassStatus(
//            @RequestParam(value = "ClassID", defaultValue = "1") String classID
//    ) {
//        ResponseEntity responseEntity = null;
//        try {
////            List<ErrorTerm> errorTermList = errorTermRepository.findByTermName(termName);
////            responseEntity = ResponseEntity.status(HttpStatus.OK).body(errorTermList);
//        } catch (Exception e) {
//            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("读取错误表中的term操作失败。。。"));
//        }
//        return responseEntity;
//    }



}
