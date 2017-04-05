package com.xjtu.dependency.ranktext;

/**
 * Created by shilei on 2017/3/22.
 */
public class Term {
    private Long termID;

    private String termName;

    private String termText;


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

    public String getTermText() {
        return termText;
    }

    public void setTermText(String termText) {
        this.termText = termText;
    }
}
