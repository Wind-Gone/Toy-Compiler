package com.example.compiler.entity.tree;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName SyntaxTree.java
 * @Description TODO
 * @createTime 2021年06月03日 11:27:00
 */
public class SyntaxTree {
    private TreeNode root;                      // 语法树的根节点

    public SyntaxTree(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void preOrder() {
        preOrderPrint(root);
    }

    public void preOrderPrint(TreeNode root) {
        System.out.println(root.getValue());
        Queue<TreeNode> myQueue = new LinkedList<>();
        List<Pair<String, Integer>> res = new ArrayList<>();
        myQueue.offer(root);
        while (!myQueue.isEmpty()) {
            TreeNode curNode = myQueue.remove();
            System.out.println(curNode.getValue());
            res.add(new Pair<>(curNode.getValue(), curNode.getLevel()));
            if (!curNode.isLeaf())
                for (TreeNode treeNode : curNode.getChildren()) {
                    treeNode.setLevel(curNode.getLevel() + 1);
                    treeNode.setFather(curNode);
                    myQueue.offer(treeNode);
                }
        }
        for (Pair<String, Integer> pair : res) {
            System.out.println("第" + pair.getValue() + "层有" + pair.getKey());
        }
    }

//    public void createAttrTree(AstNode root) {
//        if (root == null)
//            return;
//        Deque<AstNode> stack = new ArrayDeque<>();
//        stack.addLast(root);
//        while (!stack.isEmpty()) {
//            AstNode node = stack.removeLast();
//            for (int i = 0; i < node.getChildren().size(); i++) {
//                stack.addLast(node.getChildren().get(i));
//                if (node.getChildren().get(i).isLeaf() && )
//            }
//        }
//    }
}
