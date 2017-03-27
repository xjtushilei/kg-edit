package com.xjtu.visualization.domain;
/**
 * @author shilei
 * @date 2016年9月13日14:57:02
 * @description 可是化返回的信息，包括边和节点
 */

import java.util.ArrayList;

public class FacetResult {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "FacetResult [nodes=" + nodes + ", edges=" + edges + "]";
    }
}
