package com.xjtu.spider.repository;

import com.xjtu.spider.domain.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuanhao on 2017/3/16.
 */
public interface TextRepository extends JpaRepository<Text, Long> {

    List<Text> findByTermID(Long termID);

    List<Text> findByTermName(String termName);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from Text t where t.fragmentContent = ?1")
    void deleteEmpty(String fragmentContent);

    @Query("select e from Text e")
    List<Text> findAllTerm();

}
