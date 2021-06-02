package com.example.compiler.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName TreeNode.java
 * @Description TODO
 * @createTime 2021年06月02日 17:31:00
 */
public class TreeNode {
    private TreeNode father;        // 父节点
    private List<TreeNode> children;    //子节点集合
    private String content; // 节点内容
    private int ProductionID;//对应产生式ID

    public TreeNode() {
        this.content = null;
        this.children = new ArrayList<>();
        this.father = null;
        this.ProductionID = -1;
    }

    public TreeNode(TreeNode father, List<TreeNode> children, String content, int productionID) {
        this.father = father;
        this.children = children;
        this.content = content;
        ProductionID = productionID;
    }

    public TreeNode getFather() {
        return father;
    }

    public void setFather(TreeNode father) {
        this.father = father;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProductionID() {
        return ProductionID;
    }

    public void setProductionID(int productionID) {
        ProductionID = productionID;
    }
}
