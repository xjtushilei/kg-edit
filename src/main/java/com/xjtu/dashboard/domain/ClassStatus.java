package com.xjtu.dashboard.domain;


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
        this.lastModifyTime = lastModifyTime;
    }


}
