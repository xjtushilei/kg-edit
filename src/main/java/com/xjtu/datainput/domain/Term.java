package com.xjtu.datainput.domain;


import javax.persistence.*;
import java.util.Date;

/**
 * Created by shilei on 2017/3/13.
 */
@Entity
@Table(name = "datainput_term")
public class Term {
    @Id
    @GeneratedValue
    private Long termID;

    private String termName;

    private String classID;
    private String className;

    private String note;

    private Date lastModifyTime;

    public Term() {
    }

    public Term(String termName, String classID, String className, String note, Date lastModifyTime) {
        this.termName = termName;
        this.classID = classID;
        this.className = className;
        this.note = note;
        this.lastModifyTime = lastModifyTime;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
