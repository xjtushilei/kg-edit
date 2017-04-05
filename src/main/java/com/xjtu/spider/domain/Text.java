package com.xjtu.spider.domain;

import javax.persistence.*;

/**
 * 爬取的文本对象
 * Created by yuanhao on 2017/3/16.
 */

@Entity
@Table(name = "spider_text")
public class Text {

    @Id
    @GeneratedValue
    private Long fragmentID;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String fragmentContent;

    private String fragmentUrl;

    private String fragmentPostTime;

    private String fragmentScratchTime;

    private Long termID;

    private String termName;

    private String facetName;

    // 定义默认构造器，否则findAll()方法报错
    public Text() {

    }

    public Text(String fragmentContent,
                String fragmentUrl, String fragmentPostTime,
                String fragmentScratchTime, Long termID, String termName,
                String facetName) {
        super();
        this.fragmentContent = fragmentContent;
        this.fragmentUrl = fragmentUrl;
        this.fragmentPostTime = fragmentPostTime;
        this.fragmentScratchTime = fragmentScratchTime;
        this.termID = termID;
        this.termName = termName;
        this.facetName = facetName;
    }

    @Override
    public String toString() {
        return "Text{" +
                "fragmentID=" + fragmentID +
                ", fragmentContent='" + fragmentContent + '\'' +
                ", fragmentUrl='" + fragmentUrl + '\'' +
                ", fragmentPostTime='" + fragmentPostTime + '\'' +
                ", fragmentScratchTime='" + fragmentScratchTime + '\'' +
                ", termID=" + termID +
                ", termName='" + termName + '\'' +
                ", facetName='" + facetName + '\'' +
                '}';
    }

    public Long getFragmentID() {
        return fragmentID;
    }

    public void setFragmentID(Long fragmentID) {
        this.fragmentID = fragmentID;
    }

    public String getFragmentContent() {
        return fragmentContent;
    }

    public void setFragmentContent(String fragmentContent) {
        this.fragmentContent = fragmentContent;
    }

    public String getFragmentUrl() {
        return fragmentUrl;
    }

    public void setFragmentUrl(String fragmentUrl) {
        this.fragmentUrl = fragmentUrl;
    }

    public String getFragmentPostTime() {
        return fragmentPostTime;
    }

    public void setFragmentPostTime(String fragmentPostTime) {
        this.fragmentPostTime = fragmentPostTime;
    }

    public String getFragmentScratchTime() {
        return fragmentScratchTime;
    }

    public void setFragmentScratchTime(String fragmentScratchTime) {
        this.fragmentScratchTime = fragmentScratchTime;
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

}
