package com.xjtu.dashboard.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by shilei on 2017/3/13.
 */
@Entity
@Table(name = "system_step_status")
public class ClassStatus {
    @Id
    private String classid;

    private String classname;

    private String datainput = "正在进行";

    private String spider = "未开始";

    private String dependency = "未开始";

    private String rectification = "未开始";

    private String store = "未开始";

    private String visualization = "未开始";

    private Date lastModifyTime;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDatainput() {
        return datainput;
    }

    public void setDatainput(String datainput) {
        this.datainput = datainput;
    }

    public String getSpider() {
        return spider;
    }

    public void setSpider(String spider) {
        this.spider = spider;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getRectification() {
        return rectification;
    }

    public void setRectification(String rectification) {
        this.rectification = rectification;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getVisualization() {
        return visualization;
    }

    public void setVisualization(String visualization) {
        this.visualization = visualization;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public ClassStatus(String classid, String className) {
        this.classid = classid;
        classname = className;
        lastModifyTime = new Date();
    }


    public ClassStatus() {
    }

    public ClassStatus(String classid, String className, String dataInput, String spider, String dependency, String rectification, String store, String visualization, Date lastModifyTime) {
        this.classid = classid;
        classname = className;
        datainput = dataInput;
        this.spider = spider;
        this.dependency = dependency;
        this.rectification = rectification;
        this.store = store;
        this.visualization = visualization;
        this.lastModifyTime = lastModifyTime;
    }


}
