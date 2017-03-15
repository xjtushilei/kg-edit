package com.xjtu.datainput.repository;

import com.xjtu.datainput.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by shilei on 2017/3/13.
 */
public interface TermRepository extends JpaRepository<Term, Long> {

    List<Term> findByClassIDAndTermName(String classID, String termName);

    List<Term> findByClassID(String classID);


    @Query("select t.termID from Term t where t.termID not in ( select r.termID from com.xjtu.datainput.domain.Relation r )")
    List<Long> findNoUse();
}
