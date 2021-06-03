package com.example.compiler.entity.gui;

import java.util.ArrayList;
import java.util.List;

public class GuiNode {
    private String id;
    private String value;
    private List<GuiNode> children;

    public GuiNode(String id, String value) {
        this.id = id;
        this.value = value;
        children = new ArrayList<>();
    }

    public void addChild(GuiNode child) {
        children.add(child);
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public List<GuiNode> getChildren() {
        return children;
    }
}
