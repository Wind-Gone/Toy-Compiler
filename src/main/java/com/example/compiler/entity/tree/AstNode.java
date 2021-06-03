package com.example.compiler.entity.tree;

import com.example.compiler.entity.gui.GuiNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName AstNode.java
 * @Description TODO
 * @createTime 2021年06月03日 11:22:00
 */
public class AstNode {
    private int fatherId;
    private int selfId;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private List<AstNode> children;
    private String value;
    private AstNode father;

    public AstNode() {
        initChildList();
    }

    public AstNode(String value) {
        this.level = 1;
        this.value = value;
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

    /* 插入一个child节点到当前节点中 */
    public void addChildNode(AstNode treeNode) {
        initChildList();
        children.add(treeNode);
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public int getSelfId() {
        return selfId;
    }

    public void setSelfId(int selfId) {
        this.selfId = selfId;
    }

    public List<AstNode> getChildren() {
        return children;
    }

    public void setChildren(List<AstNode> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AstNode getFather() {
        return father;
    }

    public void setFather(AstNode father) {
        this.father = father;
    }

}
