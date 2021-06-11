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
        preOrderPrint();
    }


    public void preOrderPrint() {
        System.out.println(root.getValue());
        Queue<TreeNode> myQueue = new LinkedList<>();
        List<Pair<String, Integer>> res = new ArrayList<>();
        List<String> leaf1 = new ArrayList<>();//未完成的dag
        myQueue.offer(root);
        while (!myQueue.isEmpty()) {
            TreeNode curNode = myQueue.remove();
            System.out.println(curNode.getValue());
            res.add(new Pair<>(curNode.getValue(), curNode.getLevel()));
            if (!curNode.isLeaf()) {
                for (TreeNode treeNode : curNode.getChildren()) {
                    treeNode.setLevel(curNode.getLevel() + 1);
                    treeNode.setFather(curNode);
                    myQueue.offer(treeNode);
                }
            }
            else{
                if (!curNode.getValue().equals("EPSILON"))
                    leaf1.add(curNode.getValue());
            }

        }
        for (Pair<String, Integer> pair : res) {
            System.out.println("第" + pair.getValue() + "层有" + pair.getKey());
        }
        for (String leaf2 : leaf1) {
            System.out.println(leaf2);
        }
    }



    public List<String> dfs() {
        List<String> res = new ArrayList<>();
        if (root == null) {
            System.out.println(1);
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.add(root);
        while (!stack.isEmpty()) {
            TreeNode tempNode = stack.peek();
            res.add(tempNode.getValue());
            stack.pop();
            if (!tempNode.isLeaf())
                for (int i = tempNode.getChildren().size() - 1; i >= 0; i--) {
                    if (tempNode.getChildren().get(i) != null)
                        stack.push(tempNode.getChildren().get(i));
                }
        }
        return res;
    }
}
