package com.xjtu.datainput.repository;

import com.xjtu.datainput.domain.Catalog;
import com.xjtu.datainput.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by shilei on 2017/3/13.
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    List<Catalog> findByParentChapterIDAndChapterIDAndChildrenChapterID(String parentChapterID, String chapterID, String childrenChapterID);
}
