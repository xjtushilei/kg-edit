package com.xjtu.dependency.web;

import com.xjtu.common.domain.Error;
import com.xjtu.common.domain.Success;
import com.xjtu.datainput.domain.Term;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.dependency.domain.Dependency;
import com.xjtu.dependency.repository.DependencyRepository;
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

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity getAll(
            @RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID
    ) {
        List<Dependency> result = new LinkedList<>();
        try {
            result = dependencyRepository.findByClassID(ClassID);
        } catch (Exception e) {
            logger.error("查找所有关系 failed。 ", e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ResponseEntity getAll(
            @RequestParam(value = "classID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID,
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
            if (dependencyRepository.findByStartTermIDAndEndTermID(dependency.getStartTermID(), dependency.getEndTermID()).size() != 0) {
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
            @RequestParam(value = "classID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String classID
    ) {
        try {
            List<Term> result = termRepository.findByClassID(classID);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (EmptyResultDataAccessException e) {
            logger.error("查询所有知识点 失败 ", e);
            return ResponseEntity.status(HttpStatus.OK).body(new Error("查询所有知识点 失败! " + e));
        }


    }

}
