package com.xjtu.spider.service;

/**
 * 分面处理
 * Created by yuanhao on 2017/3/16.
 */
public class FacetService {

    /**
     * 分面处理
     *
     * @param facet
     * @return 处理后的分面
     * @author 郭朝彤
     */
    public String getfacet(String facet) {
        facet = facet.replaceAll("：", "");
        facet = facet.replaceAll(":", "");
        facet = facet.replaceAll(":", " ");
        facet = facet.replaceAll("/", " ");
        facet = facet.replaceAll("\"", " ");
        facet = facet.replaceAll("<", " ");
        facet = facet.replaceAll(">", " ");
        facet = facet.replaceAll("|", " ");
        facet = facet.replaceAll(" ", "");
        return facet;
    }

}
