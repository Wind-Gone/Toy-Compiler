package com.example.compiler.entity.tree;

import javafx.util.Pair;

import java.util.*;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName SyntaxTree.java
 * @Description TODO
 * @createTime 2021年06月03日 11:27:00
 */
public class SyntaxTree {
    private AstNode root;

    public SyntaxTree(AstNode root) {
        this.root = root;
    }

    public AstNode getRoot() {
        return root;
    }

    public void setRoot(AstNode root) {
        this.root = root;
    }

    public void preOrder() {
        preOrderPrint(root);
    }

    public void preOrderPrint(AstNode root) {
        System.out.println(root.getValue());
        Queue<AstNode> myQueue = new LinkedList<>();
        List<Pair<String, Integer>> res = new ArrayList<>();
        myQueue.offer(root);
        while (!myQueue.isEmpty()) {
            AstNode curNode = myQueue.remove();
            System.out.println(curNode.getValue());
            res.add(new Pair<>(curNode.getValue(), curNode.getLevel()));
            if (!curNode.isLeaf())
                for (AstNode astNode : curNode.getChildren()) {
                    astNode.setLevel(curNode.getLevel() + 1);
                    myQueue.offer(astNode);
                }
        }
        for (Pair<String, Integer> pair : res) {
            System.out.println("第" + pair.getValue() + "层有" + pair.getKey());
        }
    }
}
