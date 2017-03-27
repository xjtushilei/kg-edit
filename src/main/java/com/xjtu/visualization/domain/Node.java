package com.xjtu.visualization.domain;

/**
 * @author shilei
 * @date 2016年10月13日14:57:02
 * @description 知识图谱的节点的类
 */
public class Node {
    private String label;
    private String attributes;
    private String id;
    private int size;
    private String color;

    @Override
    public String toString() {
        return "node [label=" + label + ", attributes=" + attributes + ", id=" + id + ", size=" + size + ", color="
                + color + "]";
    }

    public Node(String label, String attributes, String id, int size, String color) {
        this.label = label;
        this.setAttributes(attributes);
        this.id = id;
        this.size = size;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
