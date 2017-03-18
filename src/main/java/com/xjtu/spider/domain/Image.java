package com.xjtu.spider.domain;

import javax.persistence.*;

/**
 * 爬取的图片对象
 * Created by yuanhao on 2017/3/16.
 */

@Entity
@Table(name = "spider_image")
public class Image {

    @Id
    @GeneratedValue
    private Long imageID;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] imageContent;

    private String imageUrl;

    private String imageScratchTime;

    private Long termID;

    private String termName;

    private String facetName;

    private String imageAPI;

    // 定义默认构造器，否则findAll()方法报错
    public Image() {

    }

    public Image(byte[] imageContent, String imageUrl, String imageScratchTime, Long termID, String termName, String facetName, String imageAPI) {

        this.imageContent = imageContent;
        this.imageUrl = imageUrl;
        this.imageScratchTime = imageScratchTime;
        this.termID = termID;
        this.termName = termName;
        this.facetName = facetName;
        this.imageAPI = imageAPI;
    }

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageScratchTime() {
        return imageScratchTime;
    }

    public void setImageScratchTime(String imageScratchTime) {
        this.imageScratchTime = imageScratchTime;
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

    public String getImageAPI() {
        return imageAPI;
    }

    public void setImageAPI(String imageAPI) {
        this.imageAPI = imageAPI;
    }
}
