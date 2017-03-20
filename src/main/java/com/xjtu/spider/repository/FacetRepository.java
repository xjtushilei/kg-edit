package com.xjtu.spider.repository;

import com.xjtu.spider.domain.Facet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 分面数据库的操作：增删改查
 * Created by yuanhao on 2017/3/16.
 */
public interface FacetRepository extends JpaRepository<Facet, Long> {

    List<Facet> findByTermID(Long termID);

}
