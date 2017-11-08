package com.xjtu.spider.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 知识点对应的分面信息
 * Created by yuanhao on 2017/3/16.
 */
@Entity
@Table(name = "spider_facet")
public class Facet {

    @Id
    @GeneratedValue
    public Long facetID;

    private Long termID;

    private String termName;

    private String facetName;

    private String note;

    @Override
    public String toString() {
        return "Facet{" +
                "facetID=" + facetID +
                ", termID=" + termID +
                ", termName='" + termName + '\'' +
                ", facetName='" + facetName + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public Facet() {

    }

    public Facet(Long termID, String termName, String facetName, String note) {
        this.termID = termID;
        this.termName = termName;
        this.facetName = facetName;
        this.note = note;
    }

    public Long getFacetID() {
        return facetID;
    }

    public void setFacetID(Long facetID) {
        this.facetID = facetID;
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

    public String getFacetName() {
        return facetName;
    }

    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
