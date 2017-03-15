package com.xjtu.datainput.domain;

/**
 * Created by shilei on 2017/3/15.
 */
public class RelationCatalog {

    private Long relationID;

    private Long catalogID;

    private Long termID;

    private String termName;

    private String classID;

    private String className;

    private String parentChapterID;

    private String parentChapterName;

    private String chapterID;

    private String chapterName;

    private String childrenChapterID;
    private String childrenChapterName;

    public RelationCatalog(Long relationID, Long catalogID, Long termID, String termName, String classID, String className, String parentChapterID, String parentChapterName, String chapterID, String chapterName, String childrenChapterID, String childrenChapterName) {
        this.relationID = relationID;
        this.catalogID = catalogID;
        this.termID = termID;
        this.termName = termName;
        this.classID = classID;
        this.className = className;
        this.parentChapterID = parentChapterID;
        this.parentChapterName = parentChapterName;
        this.chapterID = chapterID;
        this.chapterName = chapterName;
        this.childrenChapterID = childrenChapterID;
        this.childrenChapterName = childrenChapterName;
    }

    public RelationCatalog() {

    }

    public Long getRelationID() {
        return relationID;
    }

    public void setRelationID(Long relationID) {
        this.relationID = relationID;
    }

    public Long getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(Long catalogID) {
        this.catalogID = catalogID;
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

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
