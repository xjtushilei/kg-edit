package com.xjtu.datainput.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by shilei on 2017/3/13.
 */
@Entity
@Table(name = "datainput_relation")
public class Relation {
    @Id
    @GeneratedValue
    private Long relationID;


    private Long catalogID;

    private Long termID;

    private String termName;

    private String classID;

    private String className;

    public Relation() {
    }

    public Relation(Long catalogID, Long termID, String termName, String classID, String className) {
        this.catalogID = catalogID;
        this.termID = termID;
        this.termName = termName;
        this.classID = classID;
        this.className = className;
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
}
