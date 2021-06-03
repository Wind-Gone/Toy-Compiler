package com.example.compiler.entity.tree;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName AstNode.java
 * @Description TODO
 * @createTime 2021年06月03日 11:22:00
 */
public class AstNode {
    private AstNode siblings;
    private AstNode children;
    private String value;
    private AstNode father;

    public AstNode getFather() {
        return father;
    }

    public void setFather(AstNode father) {
        this.father = father;
    }

    public boolean hasSiblings() {
        return siblings != null;
    }

    public boolean hasChildren() {
        return children != null;
    }

    public AstNode(String value) {
        this.value = value;
    }

    public AstNode(AstNode siblings, AstNode children, String value, AstNode father) {
        this.siblings = siblings;
        this.children = children;
        this.value = value;
        this.father = father;
    }

    public AstNode getSiblings() {
        return siblings;
    }

    public void setSiblings(AstNode siblings) {
        this.siblings = siblings;
    }

    public AstNode getChildren() {
        return children;
    }

    public void setChildren(AstNode children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
