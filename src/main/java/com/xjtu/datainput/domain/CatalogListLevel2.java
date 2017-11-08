package com.xjtu.datainput.domain;

import java.util.ArrayList;

public class CatalogListLevel2 {
    private ArrayList<CatalogListLevel3> ChildrenChapterList;
    private String ChapterID;
    private String ChapterName;

    public ArrayList<CatalogListLevel3> getChildrenChapterList() {
        return ChildrenChapterList;
    }

    public void setChildrenChapterList(ArrayList<CatalogListLevel3> childrenChapterList) {
        ChildrenChapterList = childrenChapterList;
    }

    public String getChapterID() {
        return ChapterID;
    }

    public void setChapterID(String chapterID) {
        ChapterID = chapterID;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    /**
     *
     */
    public CatalogListLevel2() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "CatalogListLevel2 [ChildrenChapterList=" + ChildrenChapterList + ", ChapterID=" + ChapterID
                + ", ChapterName=" + ChapterName + "]";
    }

    /**
     * @param childrenChapterList
     * @param chapterID
     * @param chapterName
     */
    public CatalogListLevel2(ArrayList<CatalogListLevel3> childrenChapterList, String chapterID, String chapterName) {
        super();
        ChildrenChapterList = childrenChapterList;
        ChapterID = chapterID;
        ChapterName = chapterName;
    }


}
