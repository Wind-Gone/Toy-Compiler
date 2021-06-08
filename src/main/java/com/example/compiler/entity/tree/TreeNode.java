package com.example.compiler.entity.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName AstNode.java
 * @Description TODO
 * @createTime 2021年06月03日 11:22:00
 */
@SuppressWarnings("all")
public class TreeNode {

    private String id;                                  // 节点的ID号
    private int level;                                  // 节点所在层数
    private List<TreeNode> children;                    // 节点的所有孩子节点
    private String value;                               // 节点的值
    private TreeNode father;                            // 节点的父节点

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TreeNode() {
        initChildList();
    }

    public TreeNode(String value) {
        this.level = 1;
        this.value = value;
    }

    public TreeNode(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public TreeNode getFather() {
        return father;
    }

    public void setFather(TreeNode father) {
        this.father = father;
    }


    public void initChildList() {
        if (children == null)
            children = new ArrayList<>();
    }

    public boolean isLeaf() {
        if (children == null)
            return true;
        else {
            return children.isEmpty();
        }
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
