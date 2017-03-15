package com.xjtu.datainput.repository;

import com.xjtu.datainput.domain.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by shilei on 2017/3/13.
 */
public interface RelationRepository extends JpaRepository<Relation, Long> {

    List<Relation> findByCatalogIDAndTermID(Long catalogID, Long termID);

    List<Relation> findByCatalogID(Long catalogID);


}
