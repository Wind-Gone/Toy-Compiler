package com.example.compiler.entity.tree;

import java.util.Arrays;

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
        Boolean[] printArr = new Boolean[200];
        Arrays.fill(printArr, Boolean.FALSE);
        preOrderPrint(root, 0, printArr);
    }

    public void preOrderPrint(AstNode node, int level, Boolean[] printArr) {
        if (root == null) return;
        for (int i = 0; i < level - 1; i++) {
            if (printArr[i]) {
                System.out.print("|  ");
            } else {
                System.out.print("   ");
            }
        }
        if (node == root) {
            System.out.println(node.getValue());
        } else {
            System.out.println("|__" + node.getValue());
        }
        if (node.hasChildren()) {
            if (node.hasSiblings()) {
                if (level > 0)
                    printArr[level - 1] = true;
            } else {
                if (level > 0)
                    printArr[level - 1] = false;
            }
            preOrderPrint(node.getChildren(), level + 1, printArr);
        }
        if (node.hasSiblings()) {
            preOrderPrint(node.getSiblings(), level, printArr);
        }
    }
}
