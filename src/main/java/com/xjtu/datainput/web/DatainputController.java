package com.xjtu.datainput.web;

import com.xjtu.common.domain.Error;
import com.xjtu.common.domain.Success;
import com.xjtu.datainput.domain.*;
import com.xjtu.datainput.repository.CatalogRepository;
import com.xjtu.datainput.repository.RelationRepository;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.datainput.service.GetChapterListService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by shilei on 2017/3/13.
 */

@RestController
@RequestMapping("/datainput")
public class DatainputController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private RelationRepository relationRepository;
    @Autowired
    private TermRepository termRepository;


    @RequestMapping(value = "/writeKnowledge", method = RequestMethod.GET)
    @ApiOperation(value = "写知识点", notes = "教师在线输入知识点")
    @Transactional
    public ResponseEntity writeKnowledge(@RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID,
                                         @RequestParam(value = "ClassName", defaultValue = "测试课程") String ClassName,
                                         @RequestParam(value = "TermName", defaultValue = "测试知识点") String TermName,
                                         @RequestParam(value = "ParentChapterID", defaultValue = "ParentChapterID") String ParentChapterID,
                                         @RequestParam(value = "ParentChapterName", defaultValue = "ParentChapterName") String ParentChapterName,
                                         @RequestParam(value = "ChapterID", defaultValue = "ChapterID") String ChapterID,
                                         @RequestParam(value = "ChapterName", defaultValue = "ChapterName") String ChapterName,
                                         @RequestParam(value = "ChildrenChapterID", defaultValue = "ChildrenChapterID") String ChildrenChapterID,
                                         @RequestParam(value = "ChildrenChapterName", defaultValue = "ChildrenChapterName") String ChildrenChapterName,
                                         @RequestParam(value = "Note", defaultValue = "Note") String Note
    ) {

        Term term = new Term(TermName, ClassID, ClassName, Note, new Date());

        try {

            //写知识点
            List<Term> listTerm = termRepository.findByClassIDAndTermName(ClassID, TermName);
            if (listTerm.size() > 0) {
                logger.info("知识点列表已存在知识点", listTerm.get(0).toString());
                term = listTerm.get(0);
            } else {
                term = termRepository.save(term);
            }
            //存章节
            Catalog catalog = new Catalog(ParentChapterID, ParentChapterName, ChapterID, ChapterName, ChildrenChapterID, ChildrenChapterName);
            List<Catalog> catalogList = catalogRepository.findByParentChapterIDAndChapterIDAndChildrenChapterID(ParentChapterID, ChapterID, ChildrenChapterID);
            if (catalogList.size() > 0) {
                logger.info("已存在此catalog", catalogList.get(0).toString());
                catalog = catalogList.get(0);
            } else {
                catalog = catalogRepository.save(catalog);
            }

            //存知识点对应章节。
            Relation relation = new Relation(catalog.getCatalogID(), term.getTermID(), term.getTermName(), ClassID, ClassName);

            List<Relation> relationList = relationRepository.findByCatalogIDAndTermID(catalog.getCatalogID(), term.getTermID());
            if (relationList.size() > 0) {
                logger.info("已存在此relation", catalogList.get(0).toString());
                catalog = catalogList.get(0);
            } else {
                relation = relationRepository.save(relation);
            }
            logger.info("知识点:" + TermName + "   写入成功（包括已存在）。");

        } catch (Exception e) {
            logger.error("写入知识点 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.toString()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(term);
    }


    @RequestMapping(value = "/searchTerms", method = RequestMethod.GET)
    @ApiOperation(value = "展示该章节的知识点", notes = "输入章节等信息，展示该章节的知识点，让老师能够知道已经写过哪些知识点")
    public ResponseEntity searchTerms(
            @RequestParam(value = "ParentChapterID", defaultValue = "") String ParentChapterID,
            @RequestParam(value = "ParentChapterName", defaultValue = "ParentChapterName") String ParentChapterName,
            @RequestParam(value = "ChapterID", defaultValue = "") String ChapterID,
            @RequestParam(value = "ChapterName", defaultValue = "ChapterName") String ChapterName,
            @RequestParam(value = "ChildrenChapterID", defaultValue = "") String ChildrenChapterID,
            @RequestParam(value = "ChildrenChapterName", defaultValue = "ChildrenChapterName") String ChildrenChapterName
    ) {
        List<Relation> relationList = new ArrayList<>();
        try {
            List<Catalog> catalogList = catalogRepository.findByParentChapterIDAndChapterIDAndChildrenChapterID(ParentChapterID, ChapterID, ChildrenChapterID);
            if (catalogList.size() > 0) {
                Catalog catalog = catalogList.get(0);
                relationList = relationRepository.findByCatalogID(catalog.getCatalogID());
            }
        } catch (Exception e) {
            logger.error("展示该章节的知识点 failed。 ", e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(relationList);
    }


    @RequestMapping(value = "/deleteReletionByRelationID", method = RequestMethod.GET)
    @ApiOperation(value = "删除知识点", notes = "删除知识点")
    public ResponseEntity deleteReletionByCatalogID(
            @RequestParam(value = "relationID", defaultValue = "123") Long relationID
    ) {

        try {
            relationRepository.delete(relationID);
            //删除没用的信息
            termRepository.findNoUse().iterator().forEachRemaining(id -> {
                termRepository.delete(id);
            });
            catalogRepository.findNoUse().iterator().forEachRemaining(id -> {
                catalogRepository.delete(id);
            });
        } catch (Exception e) {
            logger.error("删除relation failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("删除失败！" + e.toString()));
        }
        logger.info("删除relation 成功。 ");
        return ResponseEntity.status(HttpStatus.OK).body(new Success("删除成功！"));
    }


    @RequestMapping(value = "/getChapterListByClassID", method = RequestMethod.GET)
    @ApiOperation(value = "展示章节列表", notes = "输入章节等信息，展示该章节的知识点，让老师能够知道已经写过哪些知识点")
    public ResponseEntity getChapterListByClassID(
            @RequestParam(value = "CourseID", defaultValue = "e63f581c-3eef-4fb9-a120-f57940ef9609") String CourseID
    ) {
        ArrayList<CatalogListLevel1> result = new ArrayList<>();
        try {
            result = new GetChapterListService().get(CourseID);
        } catch (Exception e) {
            logger.error("获取章节列表失败 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("获取章节列表失败" + e.toString()));
        }
        logger.info("获取章节列表 成功。 ");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @RequestMapping(value = "/modifyRelation", method = RequestMethod.GET)
    @ApiOperation(value = "modify一个知识点", notes = "修改一个知识点")
    @Transactional
    public ResponseEntity modifyRelation(
            @RequestParam(value = "relationID", defaultValue = "123") Long relationID,
            @RequestParam(value = "classID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID,
            @RequestParam(value = "className", defaultValue = "测试课程") String ClassName,
            @RequestParam(value = "newTermName", defaultValue = "NewTermName") String NewTermName,
            @RequestParam(value = "termName", defaultValue = "测试知识点") String TermName,
            @RequestParam(value = "parentChapterID", defaultValue = "ParentChapterID") String ParentChapterID,
            @RequestParam(value = "parentChapterName", defaultValue = "ParentChapterName") String ParentChapterName,
            @RequestParam(value = "chapterID", defaultValue = "ChapterID") String ChapterID,
            @RequestParam(value = "chapterName", defaultValue = "ChapterName") String ChapterName,
            @RequestParam(value = "childrenChapterID", defaultValue = "ChildrenChapterID") String ChildrenChapterID,
            @RequestParam(value = "childrenChapterName", defaultValue = "ChildrenChapterName") String ChildrenChapterName,
            @RequestParam(value = "note", defaultValue = "Note") String Note
    ) {

        try {
            deleteReletionByCatalogID(relationID);
        } catch (Exception e) {
            logger.error("删除relation failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("修改失败！(因删除之前的出现问题！请刷新重试)" + e.toString()));
        }
        try {
            writeKnowledge(ClassID, ClassName, NewTermName, ParentChapterID, ParentChapterName, ChapterID, ChapterName, ChildrenChapterID, ChildrenChapterName, Note);
        } catch (Exception e) {
            logger.error("修改relation failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("修改失败！" + e.toString()));
        }
        logger.info("修改relation 成功。 ");
        return ResponseEntity.status(HttpStatus.OK).body(new Success("修改成功！"));
    }

    @RequestMapping(value = "/getRelationAndTermCount", method = RequestMethod.GET)
    public ResponseEntity getRelationAndTermCount(
            @RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID
    ) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            //                new RelationCatalog()
            result.put("termCount", termRepository.findByClassID(ClassID));
            result.put("relation", relationRepository.findRelationByClassID(ClassID));
        } catch (Exception e) {
            logger.error("查找本章节下知识点 failed。 ", e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
