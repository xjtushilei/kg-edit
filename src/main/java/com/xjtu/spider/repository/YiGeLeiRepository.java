package com.xjtu.spider.repository;

import com.xjtu.spider.domain.YiGeLei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by shilei on 2017/3/13.
 */
public interface YiGeLeiRepository extends JpaRepository<YiGeLei, Long> {

    YiGeLei findByName(String name);


}
