package com.xjtu.spider.repository;

import com.xjtu.spider.domain.Facet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分面数据库的操作：增删改查
 * Created by yuanhao on 2017/3/16.
 */
public interface FacetRepository extends JpaRepository<Facet, Long> {

    List<Facet> findByTermID(Long termID);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from Facet f where f.termID = ?1 and f.facetName = ?2")
    void deleteByTermIDAndFacetName(Long termID, String facetName);

//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("update Facet f set facetName = ?3 where f.termID = ?1 and f.facetID = ?2")
//    void updateByTermIDAndFacetIDAndFacetName(Long termID, Long facetID, String facetName);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Facet f set facetName = ?3 where f.termID = ?1 and f.facetName = ?2")
    void updateByTermIDAndFacetName(Long termID, String oldFacetName, String newFacetName);

}
