package com.xjtu.datainput.repository;

import com.xjtu.datainput.domain.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by shilei on 2017/3/13.
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    List<Catalog> findByParentChapterIDAndChapterIDAndChildrenChapterID(String parentChapterID, String chapterID, String childrenChapterID);

    Catalog findByCatalogID(Long catalogID);


    @Query("select c.catalogID from Catalog c where c.catalogID not in ( select r.catalogID from com.xjtu.datainput.domain.Relation r )")
    List<Long> findNoUse();
}
