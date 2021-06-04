package com.example.compiler.entity.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * The node Class used for Front end GUI display
 **/
public class GuiNode {
    private final String id;                    // Node Id
    private final String value;                 // Node的值
    private final List<GuiNode> children;       //所有的子节点

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
