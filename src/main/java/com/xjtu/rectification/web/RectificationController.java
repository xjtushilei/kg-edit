package com.xjtu.rectification.web;

import com.xjtu.common.domain.Error;
import com.xjtu.datainput.domain.Term;
import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.dependency.domain.Dependency;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/rectification")
public class RectificationController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TermRepository termRepository;

    @RequestMapping(value = "/getClassTerm", method = RequestMethod.GET)
    public ResponseEntity getClassTerm(
            @RequestParam(value = "ClassID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String ClassID
    ) {
        List<Term> result = new LinkedList<>();
        try {
            result = termRepository.findByClassID(ClassID);
        } catch (Exception e) {
            logger.error("查找所有知识点 failed。 ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("查找所有知识点 失败！"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
