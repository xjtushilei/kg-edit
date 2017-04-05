package com.xjtu.visualization.domain;

/**
 * @author shilei
 * @date 2016年9月13日14:57:02
 * @description 知识图谱的边的类
 */

public class Edge {
    private String sourceID;
    private String attributes;
    private String targetID;
    private int size;

    public Edge(String sourceID, String attributes, String targetID, int size) {
        this.sourceID = sourceID;
        this.attributes = attributes;
        this.targetID = targetID;
        this.size = size;
    }

    @Override
    public String toString() {
        return "edge [sourceID=" + sourceID + ", attributes=" + attributes + ", targetID=" + targetID + ", size=" + size
                + "]";
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getTargetID() {
        return targetID;
    }

    public void setTargetID(String targetID) {
        this.targetID = targetID;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
