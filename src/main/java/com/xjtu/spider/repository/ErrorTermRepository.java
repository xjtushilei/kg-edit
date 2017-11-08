package com.xjtu.spider.repository;

import com.xjtu.datainput.domain.RelationCatalog;
import com.xjtu.spider.domain.ErrorTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 错误知识点数据库的操作：增删改查
 * Created by yuanhao on 2017/3/16.
 */
@SuppressWarnings("ALL")
public interface ErrorTermRepository extends JpaRepository<ErrorTerm, Long> {

    @Query("select new com.xjtu.datainput.domain.RelationCatalog(r.relationID, r.catalogID, r.termID, r.termName, r.classID, r.className," +
            " e.parentChapterID, e.parentChapterName, e.chapterID, e.chapterName, e.childrenChapterID, e.childrenChapterName) " +
            "from Relation r ,ErrorTerm e " +
            "where  " +
            "r.catalogID=e.catalogID " +
            "and r.termID=e.termID " +
            "and r.classID=?1")
    List<RelationCatalog> findErrorByClassID(String ClassID);

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
