package com.example.compiler.intermediateCodeGeneration;

import com.example.compiler.entity.token.TokenType;
import com.example.compiler.entity.tree.TreeNode;
import com.example.compiler.llParser.NonTerminalType;
import javafx.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.compiler.llParser.NonTerminalType.ASSGSTMT;
import static com.example.compiler.llParser.NonTerminalType.BOOLEXPR;

@SuppressWarnings("all")
@Deprecated
public class Dag {
    private List<TreeNode> assgstment;
    private List<TreeNode> boolexpr;

    public Dag(TreeNode root) {
        assgstment = new ArrayList<>();
        boolexpr = new ArrayList<>();
        setAssgstment(root);
        //setBoolexpr(root);
    }

    public void setAssgstment(TreeNode root) {
        System.out.println("--------findNonTerminalType开始------");
        findNonTerminalType(ASSGSTMT, root, assgstment);
    }

    public void setBoolexpr(TreeNode root) {
        findNonTerminalType(BOOLEXPR, root, boolexpr);

    }

    public void findNonTerminalType(NonTerminalType nonTerminalType, TreeNode root,
                                    List<TreeNode> nonTerminalTypeList) {
        Queue<TreeNode> myQueue = new LinkedList<>();
        List<Pair<String, Integer>> res = new ArrayList<>();
        myQueue.offer(root);
        while (!myQueue.isEmpty()) {
            TreeNode curNode = myQueue.remove();
            //System.out.println(curNode.getValue());
            res.add(new Pair<>(curNode.getValue(), curNode.getLevel()));
            if (curNode.getValue().equals(nonTerminalType.toString().toLowerCase())) {
                nonTerminalTypeList.add(curNode);
                System.out.println(curNode.getValue());
            }
            if (!curNode.isLeaf())
                for (TreeNode treeNode : curNode.getChildren()) {
                    treeNode.setLevel(curNode.getLevel() + 1);
                    treeNode.setFather(curNode);
                    myQueue.offer(treeNode);
                }
        }
        System.out.println("--------findNonTerminalType结束------");
        /*
        for (Pair<String, Integer> pair : res) {
            System.out.println("第" + pair.getValue() + "层有" + pair.getKey());
        }
         */
    }


    public void assgDag() {
        for (TreeNode assg : assgstment) {
            System.out.println("--------转换开始------");
            assgTreeNode2Dag(assg);
            System.out.println("--------转换结束------");
        }
    }

    public List<String> leafCollect(TreeNode root) {

        Queue<TreeNode> myQueue = new LinkedList<>();
        List<Pair<String, Integer>> res = new ArrayList<>();
        List<String> leaf = new ArrayList<>();
        myQueue.offer(root);
        while (!myQueue.isEmpty()) {
            TreeNode curNode = myQueue.remove();
            res.add(new Pair<>(curNode.getValue(), curNode.getLevel()));
            if (!curNode.isLeaf()) {
                for (TreeNode treeNode : curNode.getChildren()) {
                    treeNode.setLevel(curNode.getLevel() + 1);
                    treeNode.setFather(curNode);
                    myQueue.offer(treeNode);
                }
            } else {
                if (!curNode.getValue().equals("EPSILON"))
                    leaf.add(curNode.getValue());
            }
        }
        return leaf;

    }

    public List<String> postOrderLeaf(List<String> leaf) {
        Stack<String> temsymbol = new Stack<>();
        List<String> newLeaf = new ArrayList<>();
        int flag = 0;
        for (String s : leaf) {
            switch (s) {
                case "-":
                case "+":
                case "=":
                    //符号运算层数越低优先级越高
                    System.out.println("------symbol:" + s);
                    temsymbol.push(s);
                    flag = 0;
                    break;
                case "*":
                case "/":
                    System.out.println("------symbol:" + s);
                    temsymbol.push(s);
                    flag = 1;

                    break;
                case ";":
                    break;
                default:
                    System.out.println("------ numOrID:" + s);
                    newLeaf.add(s);
                    if (flag == 1) {
                        newLeaf.add(temsymbol.peek());
                        temsymbol.pop();
                        flag = 0;
                    }
                    break;
            }
        }
        while (!temsymbol.empty()) {
            newLeaf.add(temsymbol.pop());
        }
        return newLeaf;
    }


    public void assgTreeNode2Dag(TreeNode root) {
        Queue<TreeNode> myQueue = new LinkedList<>();
        List<Pair<String, Integer>> res = new ArrayList<>();
        List<String> leaf = leafCollect(root);
        Stack<String> symbolPreOder = new Stack<>();

        System.out.println("--------Leaf收集------");
        for (String s : leaf) {
            System.out.println(s);
        }

        System.out.println("-----Leaf重整-----");
        List<String> newLeaf = postOrderLeaf(leaf);

        System.out.println("-----Leaf后序输出-----");
        for (String s : newLeaf) {
            System.out.print(s + " ");
        }
        System.out.println();


        //接下来生成leaf和node链

        List<Object> DagList = new ArrayList<>();
        Stack<Object> formula = new Stack<>();
        String num = "([num]+):(\\d+)(.*)";
        String id = "([identifiers]+):(.*)";
        int i = 1;
        int flag = 0;
        for (String core : newLeaf) {
            //正则匹配读取num的值或id
            Pattern numPattern = Pattern.compile(num);
            Pattern idPattern = Pattern.compile(id);
            Matcher numMatcher = numPattern.matcher(core);
            Matcher idMatcher = idPattern.matcher(core);
            //未考虑有重复的情况
            //num
            if (numMatcher.find()) {
                TokenType tokenType = TokenType.NUM;
                int no = Integer.parseInt(numMatcher.group(2));
                System.out.println("NUM:" + no);
                Leaf temLeaf = new Leaf(tokenType, no);
                temLeaf.setId(i);
                i++;
                DagList.add(temLeaf);
                formula.push(temLeaf);
                flag = 0;
            }
            //id
            else if (idMatcher.find()) {
                TokenType tokenType = TokenType.IDENTIFIERS;
                String entry = idMatcher.group(2);
                System.out.println("entry:" + entry);
                Leaf temLeaf = new Leaf(tokenType, entry);
                temLeaf.setId(i);
                i++;
                DagList.add(temLeaf);
                formula.push(temLeaf);
                flag = 0;
            } else {
                System.out.println("------symbol:" + core);
                Object rightObject = formula.pop();
                Object leftObject = formula.pop();
                int left = -1, right = -1;
                if (rightObject instanceof Leaf) {
                    Leaf rightLeaf = (Leaf) rightObject;
                    right = rightLeaf.getId();
                } else if (rightObject instanceof Node) {
                    Node rightNode = (Node) rightObject;
                    right = rightNode.getId();
                }
                if (leftObject instanceof Leaf) {
                    Leaf leftLeaf = (Leaf) leftObject;
                    left = leftLeaf.getId();
                } else if (leftObject instanceof Node) {
                    Node leftNode = (Node) leftObject;
                    left = leftNode.getId();
                }
                Node node = new Node(core, left, right);
                node.setId(i);
                i++;
                DagList.add(node);
                formula.push(node);
            }

        }


        for (Object object : DagList) {
            if (object instanceof Leaf) {
                Leaf dagLeaf = (Leaf) object;
                System.out.println(dagLeaf);

            } else if (object instanceof Node) {
                Node dagNode = (Node) object;
                System.out.println(dagNode);

            }
        }
    }

}
