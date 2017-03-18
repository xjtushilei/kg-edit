package com.xjtu.spider.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 错误的知识点信息
 * Created by yuanhao on 2017/3/16.
 */

@Entity
@Table(name = "spider_errorTerm")
public class ErrorTerm {

    @Id
    @GeneratedValue
    private Long errorTermID;

    private Long termID;

    private String termName;

    private Long catalogID;

    private String parentChapterID;

    private String parentChapterName;

    private String chapterID;

    private String chapterName;

    private String childrenChapterID;

    private String childrenChapterName;

    @Override
    public String toString() {
        return "ErrorTerm{" +
                "errorTermID=" + errorTermID +
                ", termID=" + termID +
                ", termName='" + termName + '\'' +
                ", catalogID=" + catalogID +
                ", parentChapterID='" + parentChapterID + '\'' +
                ", parentChapterName='" + parentChapterName + '\'' +
                ", chapterID='" + chapterID + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", childrenChapterID='" + childrenChapterID + '\'' +
                ", childrenChapterName='" + childrenChapterName + '\'' +
                '}';
    }

    // 定义默认构造器，否则findAll()方法报错
    public ErrorTerm() {

    }

    public ErrorTerm(Long termID, String termName, Long catalogID, String parentChapterID, String parentChapterName, String chapterID, String chapterName, String childrenChapterID, String childrenChapterName) {
        this.termID = termID;
        this.termName = termName;
        this.catalogID = catalogID;
        this.parentChapterID = parentChapterID;
        this.parentChapterName = parentChapterName;
        this.chapterID = chapterID;
        this.chapterName = chapterName;
        this.childrenChapterID = childrenChapterID;
        this.childrenChapterName = childrenChapterName;
    }

    public Long getErrorTermID() {
        return errorTermID;
    }

    public void setErrorTermID(Long errorTermID) {
        this.errorTermID = errorTermID;
    }

    public Long getTermID() {
        return termID;
    }

    public void setTermID(Long termID) {
        this.termID = termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public Long getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(Long catalogID) {
        this.catalogID = catalogID;
    }

    public String getParentChapterID() {
        return parentChapterID;
    }

    public void setParentChapterID(String parentChapterID) {
        this.parentChapterID = parentChapterID;
    }

    public String getParentChapterName() {
        return parentChapterName;
    }

    public void setParentChapterName(String parentChapterName) {
        this.parentChapterName = parentChapterName;
    }

    public String getChapterID() {
        return chapterID;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChildrenChapterID() {
        return childrenChapterID;
    }

    public void setChildrenChapterID(String childrenChapterID) {
        this.childrenChapterID = childrenChapterID;
    }

    public String getChildrenChapterName() {
        return childrenChapterName;
    }

    public void setChildrenChapterName(String childrenChapterName) {
        this.childrenChapterName = childrenChapterName;
    }
}
