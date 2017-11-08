package com.xjtu.datainput.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by shilei on 2017/3/13.
 */
@Entity
@Table(name = "datainput_catalog")
public class Catalog {
    @Id
    @GeneratedValue
    private Long catalogID;

    private String parentChapterID;

    private String parentChapterName;

    private String chapterID;

    private String chapterName;

    private String childrenChapterID;
    private String childrenChapterName;

    public Catalog() {
    }

    public Catalog(String parentChapterID, String parentChapterName, String chapterID, String chapterName, String childrenChapterID, String childrenChapterName) {
        this.parentChapterID = parentChapterID;
        this.parentChapterName = parentChapterName;
        this.chapterID = chapterID;
        this.chapterName = chapterName;
        this.childrenChapterID = childrenChapterID;
        this.childrenChapterName = childrenChapterName;
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
