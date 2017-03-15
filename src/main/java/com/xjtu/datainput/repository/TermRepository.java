package com.xjtu.datainput.repository;

import com.xjtu.datainput.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by shilei on 2017/3/13.
 */
public interface TermRepository extends JpaRepository<Term, Long> {

    List<Term> findByClassIDAndTermName(String classID, String termName);

    List<Term> findByClassID(String classID);

}
