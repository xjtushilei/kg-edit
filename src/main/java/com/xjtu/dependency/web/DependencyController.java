package com.xjtu.dependency.web;

import com.xjtu.common.domain.Error;
import com.xjtu.common.domain.Success;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.dependency.domain.Dependency;
import com.xjtu.dependency.ranktext.RankText;
import com.xjtu.dependency.ranktext.Term;
import com.xjtu.dependency.repository.DependencyRepository;
import com.xjtu.dependency.service.DependencyService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shilei on 2017/3/13.
 */

@RestController
@RequestMapping("/dependency")
public class DependencyController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DependencyRepository dependencyRepository;
    @Autowired
    private TermRepository termRepository;

    @Autowired
    private DependencyService dependencyService;


    @RequestMapping(value = "/startAlgorithm", method = RequestMethod.GET)
    public ResponseEntity startAlgorithm(
            @RequestParam(value = "ClassID", defaultValue = "a7a6e4b7-e5d1-42a4-b7d5-0f7c6a7ff9e5") String ClassID
    ) {
        try {
            List<Term> termList = dependencyService.getTermList(ClassID);
            List<Dependency> dependencyList = new RankText().rankText(termList, ClassID, termList.size() * 2);
            for (Dependency d : dependencyList) {
                add(ClassID, d.getStartTermName(), d.getStartTermID(), d.getEndTermName(), d.getEndTermID(), d.getConfidence());
            }
        } catch (Exception e) {
            logger.error("算法分析 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("算法分析失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("算法分析结束！"));
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity getAll(
            @RequestParam(value = "ClassID", defaultValue = "a7a6e4b7-e5d1-42a4-b7d5-0f7c6a7ff9e5") String ClassID
    ) {
        List<Dependency> result = new LinkedList<>();
        try {
            result = dependencyRepository.findByClassID(ClassID);
        } catch (Exception e) {
            logger.error("查找所有关系 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("查找所有关系失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ResponseEntity add(
            @RequestParam(value = "classID", defaultValue = "a7a6e4b7-e5d1-42a4-b7d5-0f7c6a7ff9e5") String ClassID,
            @RequestParam(value = "startTermName", defaultValue = "一个人") String startTermName,
            @RequestParam(value = "startTermID", defaultValue = "23") Long startTermID,
            @RequestParam(value = "endTermName", defaultValue = "123") String endTermName,
            @RequestParam(value = "endTermID", defaultValue = "24") Long endTermID,
            @RequestParam(value = "confidence", defaultValue = "1.0") Float confidence
    ) {
        if (startTermID.compareTo(endTermID) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("两个知识点相同！不能添加！"));
        }
        try {
            Dependency dependency = new Dependency(ClassID, startTermName, startTermID, endTermName, endTermID, confidence);
            //            logger.info(startTermID+"-"+endTermID);
            if (dependencyRepository.findByStartTermIDAndEndTermID(dependency.getStartTermID(), dependency.getEndTermID()).size() != 0) {
                logger.error("该条关系 已存在！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("该条关系 已存在！"));
            } else {
                dependencyRepository.save(dependency);
                return ResponseEntity.status(HttpStatus.OK).body(new Success("人工增加一条关系 成功！"));
            }

        } catch (Exception e) {
            logger.error("增加一条关系 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("人工增加一条关系 失败"));
        }


    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseEntity delete(
            @RequestParam(value = "dependencyid", defaultValue = "1") Long dependencyid
    ) {
        try {
            dependencyRepository.delete(dependencyid);
        } catch (EmptyResultDataAccessException e) {
            logger.error("删除 dependencyid：【" + dependencyid + " 】 失败 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("该条记录不存在！无法删除！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new Success("删除成功！"));
    }

    @RequestMapping(value = "/getClassTerms", method = RequestMethod.GET)
    public ResponseEntity getClassTerms(
            @RequestParam(value = "classID", defaultValue = "a7a6e4b7-e5d1-42a4-b7d5-0f7c6a7ff9e5") String classID
    ) {
        try {
            List<com.xjtu.datainput.domain.Term> result = termRepository.findByClassID(classID);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (EmptyResultDataAccessException e) {
            logger.error("查询所有知识点 失败 ", e);
            return ResponseEntity.status(HttpStatus.OK).body(new Error("查询所有知识点 失败! " + e));
        }


    }

}
