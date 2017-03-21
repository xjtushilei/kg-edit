package com.xjtu.dependency.domain;


import javax.persistence.*;


/**
 * Created by shilei on 2017/3/13.
 */
@Entity
@Table(name = "dependency")
public class Dependency {

    @Id
    @GeneratedValue
    private Long dependencyID;

    private String classID;
    private String startTermName;
    private Long startTermID;
    private String endTermName;
    private Long endTermID;
    private Float confidence;


    public Dependency() {
    }

    public Dependency(String classID, String startTermName, Long startTermID, String endTermName, Long endTermID, Float confidence) {
        this.classID = classID;
        if (startTermID < endTermID) {
            this.startTermName = startTermName;
            this.startTermID = startTermID;
            this.endTermName = endTermName;
            this.endTermID = endTermID;
        } else {
            this.startTermName = endTermName;
            this.startTermID = endTermID;
            this.endTermName = startTermName;
            this.endTermID = startTermID;
        }
        this.confidence = confidence;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getStartTermName() {
        return startTermName;
    }

    public void setStartTermName(String startTermName) {
        this.startTermName = startTermName;
    }

    public Long getStartTermID() {
        return startTermID;
    }

    public void setStartTermID(Long startTermID) {
        this.startTermID = startTermID;
    }

    public String getEndTermName() {
        return endTermName;
    }

    public void setEndTermName(String endTermName) {
        this.endTermName = endTermName;
    }

    public Long getEndTermID() {
        return endTermID;
    }

    public void setEndTermID(Long endTermID) {
        this.endTermID = endTermID;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Long getDependencyID() {
        return dependencyID;
    }

    public void setDependencyID(Long dependencyID) {
        this.dependencyID = dependencyID;
    }
}
