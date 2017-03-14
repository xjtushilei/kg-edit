package com.xjtu.spider.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by shilei on 2017/3/13.
 */

@Entity
public class YiGeLei {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer year;

    private String text;

    private Date spiderdate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSpiderdate() {
        return spiderdate;
    }

    public void setSpiderdate(Date spiderdate) {
        this.spiderdate = spiderdate;
    }

    public YiGeLei() {
    }

    public YiGeLei(String name, Integer year, String text, Date spiderdate) {
        this.name = name;
        this.year = year;
        this.text = text;
        this.spiderdate = spiderdate;
    }
}
