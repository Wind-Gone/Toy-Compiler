package com.example.compiler.entity.gui;

import java.util.ArrayList;
import java.util.List;

public class GuiNode {
    private String id;
    private List<GuiNode> children;
    public GuiNode(String id){
        this.id = id;
        children=new ArrayList<>();
    }
    public void addChild(GuiNode child){
        children.add(child);
    }
}
