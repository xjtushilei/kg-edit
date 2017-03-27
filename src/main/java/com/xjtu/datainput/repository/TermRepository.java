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

    Term findByTermID(Long TermID);


    @Query("select t.termID from Term t where t.termID not in ( select r.termID from com.xjtu.datainput.domain.Relation r )")
    List<Long> findNoUse();

    @Query("select t " +
            "from Term t, Catalog c , Relation r " +
            "where " +
            "c.parentChapterID=?1 and c.chapterID=?2 and c.childrenChapterID=?3 and " +
            "c.catalogID=r.catalogID and " +
            "r.termID=t.termID")
    List<Term> findByCatalog(String parentChapterID, String chapterID, String childrenChapterID);
}
