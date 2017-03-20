package com.xjtu.dashboard.repository;

import com.xjtu.dashboard.domain.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by shilei on 2017/3/13.
 */
public interface ClassStatusRepository extends JpaRepository<ClassStatus, Long> {

    List<ClassStatus> findBySpider(String spider);

}
