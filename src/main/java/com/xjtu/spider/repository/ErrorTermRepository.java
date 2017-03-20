package com.xjtu.spider.repository;

import com.xjtu.datainput.domain.RelationCatalog;
import com.xjtu.spider.domain.ErrorTerm;
import com.xjtu.spider.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuanhao on 2017/3/16.
 */
public interface ErrorTermRepository extends JpaRepository<ErrorTerm, Long> {

    List<ErrorTerm> findByErrorTermID(Long errorTermID);

    List<ErrorTerm> findByTermID(Long termID);

    List<ErrorTerm> findByTermName(String termName);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update ErrorTerm e set e.termName = ?1 where e.termName = ?2")
    void updateErrorTerm(String newTermName, String oldTermName);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from ErrorTerm e where e.termID = ?1")
    void deleteErrorTerm(Long termID);

}
