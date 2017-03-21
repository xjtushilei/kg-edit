package com.xjtu.dependency.repository;


import com.xjtu.dependency.domain.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by shilei on 2017/3/21.
 */
public interface DependencyRepository extends JpaRepository<Dependency, Long> {

    List<Dependency> findByClassID(String ClassID);

    List<Dependency> findByStartTermIDAndEndTermID(Long startTermID, Long endTermID);
}
