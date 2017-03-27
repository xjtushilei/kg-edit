package com.xjtu.dependency.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startTermID == null) ? 0 : startTermID.hashCode());
        result = prime * result + ((endTermID == null) ? 0 : endTermID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dependency other = (Dependency) obj;
        if (startTermID == null) {
            if (other.startTermID != null)
                return false;
        } else if (!startTermID.equals(other.startTermID))
            return false;
        if (endTermID == null) {
            if (other.endTermID != null)
                return false;
        } else if (!endTermID.equals(other.endTermID))
            return false;
        return true;
    }

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
