package com.xjtu.spider.web;

import com.xjtu.common.domain.Error;
import com.xjtu.common.domain.Success;
import com.xjtu.dashboard.domain.ClassStatus;
import com.xjtu.dashboard.repository.ClassStatusRepository;
import com.xjtu.datainput.domain.Term;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.spider.domain.ErrorTerm;
import com.xjtu.spider.domain.SpiderStatus;
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
    @Autowired
    private ClassStatusRepository classStatusRepository;

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
                storeController.storeSingleTermText(term.getTermID(), term.getTermName());
                logger.info("爬取知识点文本结束。。。" + term.getTermName());
//                // 获取每个知识点的图片
//                logger.info("爬取知识点图片开始。。。" + term.getTermName());
//                storeController.storeSingleTermImage(term.getTermID(), term.getTermName());
//                logger.info("爬取知识点图片结束。。。" + term.getTermName());
//                // 获取每个知识点的分面
//                logger.info("爬取知识点分面开始。。。" + term.getTermName());
//                storeController.storeSingleTermFacet(term.getTermID(), term.getTermName());
//                logger.info("爬取知识点分面结束。。。" + term.getTermName());
            }

//            // 删除空文本和空图片
//            storeController.emptyText();
//            storeController.emptyImage();

//            // 读取图片数据表，将每个图片的内容都成对应的API形式保存到对应字段。
//            logger.info("设置知识点图片表中所有图片的API开始。。。");
//            storeController.getImageAllApi();
//            logger.info("爬取知识点图片表中所有图片的API结束。。。");


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


    @RequestMapping(value = "/getClassStatus", method = RequestMethod.GET)
    @ApiOperation(value = "读取所有门课程的爬取进度", notes = "读取系统进度表格，返回所有课程现在的爬取情况")
    public ResponseEntity getClassStatus() {
        ResponseEntity responseEntity = null;
        List<ClassStatus> classStatusList = new ArrayList<>();
        try {
            // 找到所有正在运行爬虫的课程
            classStatusList = classStatusRepository.findBySpider("正在执行");
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



}
