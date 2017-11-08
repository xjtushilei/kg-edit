package com.xjtu.datainput.domain;

import java.util.ArrayList;

public class CatalogListLevel1 {
    private String ParentChapterID;
    private ArrayList<CatalogListLevel2> ChapterList;
    private String ParentChapterName;

    @Override
    public String toString() {
        return "CatalogListLevel1 [ParentChapterID=" + ParentChapterID + ", ChapterList=" + ChapterList + ", ParentChapterName="
                + ParentChapterName + "]";
    }

    /**
     * @param parentChapterID
     * @param ChapterList
     * @param parentChapterName
     */
    public CatalogListLevel1(String parentChapterID, ArrayList<CatalogListLevel2> ChapterList, String parentChapterName) {
        super();
        ParentChapterID = parentChapterID;
        this.ChapterList = ChapterList;
        ParentChapterName = parentChapterName;
    }

    /**
     *
     */
    public CatalogListLevel1() {
        super();
    }

    public String getParentChapterID() {
        return ParentChapterID;
    }

    public void setParentChapterID(String parentChapterID) {
        ParentChapterID = parentChapterID;
    }

    public ArrayList<CatalogListLevel2> getChapterList() {
        return ChapterList;
    }

    public void setChapterList(ArrayList<CatalogListLevel2> ChapterList) {
        this.ChapterList = ChapterList;
    }

    public String getParentChapterName() {
        return ParentChapterName;
    }

    public void setParentChapterName(String parentChapterName) {
        ParentChapterName = parentChapterName;
    }

}
