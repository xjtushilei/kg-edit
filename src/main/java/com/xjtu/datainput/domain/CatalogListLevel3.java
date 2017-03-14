package com.xjtu.datainput.domain;

public class CatalogListLevel3 {
    private String ChildrenChapterID;
    private String ChildrenChapterName;

    /**
     * @param childrenChapterID
     * @param childrenChapterName
     */
    public CatalogListLevel3(String childrenChapterID, String childrenChapterName) {
        super();
        ChildrenChapterID = childrenChapterID;
        ChildrenChapterName = childrenChapterName;
    }

    /**
     *
     */
    public CatalogListLevel3() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getChildrenChapterID() {
        return ChildrenChapterID;
    }

    public void setChildrenChapterID(String childrenChapterID) {
        ChildrenChapterID = childrenChapterID;
    }

    public String getChildrenChapterName() {
        return ChildrenChapterName;
    }

    public void setChildrenChapterName(String childrenChapterName) {
        ChildrenChapterName = childrenChapterName;
    }


}
