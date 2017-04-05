package com.xjtu.dependency.service;

import com.xjtu.datainput.repository.TermRepository;
import com.xjtu.dependency.ranktext.Term;
import com.xjtu.dependency.repository.DependencyRepository;
import com.xjtu.spider.domain.Text;
import com.xjtu.spider.repository.TextRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DependencyService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TermRepository termRepository;
    @Autowired
    private DependencyRepository dependencyRepository;
    @Autowired
    private TextRepository textRepository;

    public static void main(String[] args) {
        new DependencyService().getTermList("95DE7C2EF980");
    }

    public List<Term> getTermList(String ClassID) {
        List<Term> result = new ArrayList<>();
        List<com.xjtu.datainput.domain.Term> termList = termRepository.findTermIDByClassID(ClassID);
        logger.info(termList.size() + "term");
        for (com.xjtu.datainput.domain.Term t : termList) {
            List<Text> textList = textRepository.findTextByTermID(t.getTermID());
            String content = "";
            for (Text text : textList) {
                content = content + text.getFragmentContent();
            }
            Term term = new Term();
            term.setTermID(t.getTermID());
            term.setTermName(t.getTermName());
            term.setTermText(content);
            //            logger.info(term.getTermText());
            result.add(term);
        }
        return result;
    }


}
