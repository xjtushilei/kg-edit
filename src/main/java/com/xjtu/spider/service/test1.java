package com.xjtu.spider.service;

import com.xjtu.start.StartOfOS;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by shilei on 2017/4/20.
 */
public class test1 {


    public static void main(String[] args) {
        System.setProperty("phantomjs.binary.path", StartOfOS.class.getClassLoader().getResource("").getPath() + "phantomjs/phantomjs.exe");

        WebDriver driver = new PhantomJSDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.baidu.com/");
        System.out.println(driver.getPageSource());
    }
}
