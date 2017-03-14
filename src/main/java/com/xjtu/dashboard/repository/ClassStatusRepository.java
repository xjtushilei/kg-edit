package com.xjtu.dashboard.repository;

import com.xjtu.dashboard.domain.ClassStatus;
import com.xjtu.spider.domain.YiGeLei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by shilei on 2017/3/13.
 */
public interface ClassStatusRepository extends JpaRepository<ClassStatus, Long> {


}
