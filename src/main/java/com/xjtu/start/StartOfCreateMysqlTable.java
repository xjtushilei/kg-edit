package com.xjtu.start;

import com.xjtu.dashboard.domain.ClassStatus;
import com.xjtu.dashboard.repository.ClassStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 服务启动执行的程序
 * 优先级：1>2
 */

@Component
@Order(value = 1)
public class StartOfCreateMysqlTable implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClassStatusRepository classStatusRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>>>>服务启动执行，开始检查数据库操作<<<<<<<<<<<<<");
        if (classStatusRepository.findAll().size() == 0) {
            logger.info("数据库中没有数据，开始插入测试课程");
            classStatusRepository.save(new ClassStatus("c44327d2-3511-44c2-b641-1815149772d1", "测试课程1"));
        }
    }


}

