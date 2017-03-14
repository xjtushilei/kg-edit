package com.xjtu.spider.web;

import com.xjtu.spider.config.Config;
import com.xjtu.spider.domain.YiGeLei;
import com.xjtu.spider.repository.YiGeLeiRepository;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by shilei on 2017/3/13.
 */

@RestController
@RequestMapping("/spider")
public class SpiderController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private YiGeLeiRepository yiGeLeiRepository;


    @RequestMapping(value = "/getbyname", method = RequestMethod.GET)
    @ApiOperation(value = "查找用户", notes = "这里是描述")
    public YiGeLei get(@RequestParam(value = "userName", defaultValue = "小米") String name) {

        YiGeLei result = new YiGeLei();
        try {
            result = yiGeLeiRepository.findByName(name);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("getlist failed, ", e);
            return result;
        }
        return result;
    }


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ApiOperation(value = "增加", notes = "这里是描述")
    public YiGeLei add(@RequestParam(value = "userName", defaultValue = "小米123") String name) {
        YiGeLei result = new YiGeLei(name, 123, "fsvaxfasdf士大夫", new Date());
        try {
            result = yiGeLeiRepository.save(result);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("getlist failed, ", e);
            return result;
        }
        return result;
    }

}
