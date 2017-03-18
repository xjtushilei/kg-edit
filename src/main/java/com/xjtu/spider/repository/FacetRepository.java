package com.xjtu.spider.repository;

import com.xjtu.spider.domain.Facet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by yuanhao on 2017/3/16.
 */
public interface FacetRepository extends JpaRepository<Facet, Long> {

    List<Facet> findByTermID(String termID);

}
