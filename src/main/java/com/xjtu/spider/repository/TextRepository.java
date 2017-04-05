package com.xjtu.spider.repository;

import com.xjtu.spider.domain.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文本数据库的操作：增删改查
 * Created by yuanhao on 2017/3/16.
 */
public interface TextRepository extends JpaRepository<Text, Long> {

    List<Text> findByTermID(Long termID);

    List<Text> findByTermIDAndFacetName(Long termID, String facetName);

    @Query("select t from Text t where t.termID=?1 ")
    List<Text> findTextByTermID(Long termID);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from Text t where t.termID = ?1 and t.fragmentID = ?2")
    void deleteByTermIDAndFragmentID(Long termID, Long fragmentID);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from Text t where t.termID = ?1 and t.facetName = ?2")
    void deleteByTermIDAndFacetName(Long termID, String facetName);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Text t set fragmentContent = ?3 where t.termID = ?1 and t.fragmentID = ?2")
    void updateByTermIDAndFragmentIDAndFragmentContent(Long termID, Long fragmentID, String fragmentContent);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Text t set facetName = ?3 where t.termID = ?1 and t.facetName = ?2")
    void updateByTermIDAndFacetName(Long termID, String oldFacetName, String newFacetName);

}
