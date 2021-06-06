package com.example.compiler.llParser;

import com.example.compiler.entity.ErrorCode;
import com.example.compiler.entity.WrongMessage;
import com.example.compiler.entity.gui.GuiNode;
import com.example.compiler.entity.tree.SyntaxTree;
import com.example.compiler.entity.tree.TreeNode;
import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Token;
import com.example.compiler.token.TokenType;
import javafx.util.Pair;

import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class LLParser {
    private final Grammer grammer;
    private final ParsingTable parsingTable;
    private final HashMap<String, TreeSet<String>> firstSet;            // 计算单字符串的first集
    private final HashMap<String, TreeSet<String>> followSet;           // 计算单字符串的follow集
    private final HashMap<List<Object>, TreeSet<String>> firstSet2;     // 计算字符串链表的first集
    private final List<Production> productions;                         // 存储每个非终结符的所有产生式
    private HashSet<String> VnSet = new HashSet<>();              //非终结符Vn集合
    private HashSet<String> VtSet = new HashSet<>();              //终结符Vt集合
    private final HashMap<Pair<Integer, Integer>, WrongMessage> wrongList = new HashMap<>();    //错误列表
    private final HashMap<String, ArrayList<List<Object>>> productionMap = new HashMap<>(); //所有的产生式集合
    private String[][] finalTable;              //最终的打印表
    private static String start = "PROGRAM";    // 起始符号
    private List<Token> w;              // 分析程序的输入串
    private Stack<Object> stk;          // 分析程序的栈
    private int id = 0;
    private int ip = 0;                 // 栈顶指针
    private SyntaxTree syntaxTree;      // 语法树
    private Stack<TreeNode> treeStack;   // 用于语法树输出的栈
    private List<String> values = new ArrayList<>();       //所有数字和identifier

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    public void setSyntaxTree(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public LLParser(String input) {
        treeStack = new Stack<>();
        grammer = new Grammer();
        parsingTable = new ParsingTable();
        productions = new ArrayList<>();
        firstSet = new HashMap<>();
        firstSet2 = new HashMap<>();
        followSet = new HashMap<>();
        stk_init();
        w_init(input);
//        init();
        lmDerivation();
    }

    private void stk_init() {
        stk = new Stack<>();
        stk.push(TokenType.DOLLAR);
        stk.push(NonTerminalType.PROGRAM);
        treeStack = new Stack<>();
        treeStack.push(new TreeNode(TokenType.DOLLAR.getValue()));
    }

    private void w_init(String input) {
        w = new ArrayList<>();
        Lexer lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        for (Token token : tokens) {
            TokenType type = token.getTokenType();
            if (type == TokenType.REALNUMBER || type == TokenType.EXPONENT || type == TokenType.FRACTION || type == TokenType.DIGIT || type == TokenType.INTNUMBER) {
                token.setTokenType(TokenType.NUM);
                w.add(token);
                continue;
            }
            w.add(token);
        }
        w.add(new Token(TokenType.DOLLAR));
    }

    /**
     * 基础初始化
     */
    public void init() {
        ip = 0;
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> VnSet.add(item.getValue()));
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> createProduces(item.getValue()));
        String Terminal = "OPENCURLYBRACE,CLOSECURLYBRACE,IF,OPENBRACE,CLOSEBRACE,THEN,ELSE,WHILE,IDENTIFIERS,EQUAL,SEMICOLON,LESS,GREATER,LESSEQUAL,GREATEREQUAL,EQUALEQUAL,PLUS,MINUS,MULTIPLY,DIVIDE,NUM,DOLLAR";
        VtSet = new HashSet<>(Arrays.asList(Terminal.split(",")));
    }


    /**
     * 输出最左推导 -- 书上的伪代码
     */
    private void lmDerivation() {
        ip = 0;
        Object X = stk.peek();
        int guiId = 0;
        TreeNode root = new TreeNode(String.valueOf(guiId), "program");
        treeStack.push(root);
        TreeNode node = treeStack.peek();
        guiId++;
        while (X != TokenType.DOLLAR && !treeStack.isEmpty()) {
            System.out.println("------stk-------------: " + stk);
            System.out.println("------token-------------: " + w.get(ip).getTokenType());
            TokenType a = w.get(ip).getTokenType();
            Token b = w.get(ip);
            if (X == a && node.getValue() == a.getValue()) {                      // X是终结符且匹配成功
                stk.pop();
                String pattern = "\\W";                                           // 如果该终结符不是字母/数字，就直接赋值
                boolean isMatch = Pattern.matches(pattern, b.getTokenString());
                if (isMatch)
                    node.setValue(b.getTokenString());
                else {
                    node.setValue(a.getValue() + ":" + b.getTokenString());     // 否则要加上原来token的字符值
                    values.add(b.getTokenString());
                }
                System.out.println(b.getTokenString());
                treeStack.pop();
                ip++;
            } else if (X instanceof TokenType) {                                // X是终结符且出错
                error(X, w.get(ip));
                break;
            } else if (parsingTable.get((NonTerminalType) X, a) == -1) {
                error(X, w.get(ip));
                break;
            } else {
                Production production = grammer.get(parsingTable.get((NonTerminalType) X, a));
                production.setId(id);
                productions.add(production);
                id++;
                stk.pop();
                treeStack.pop();
                List<TreeNode> childList = new ArrayList<>();
                List<Object> rightExpression = production.getRightExpression();
                for (Object o : rightExpression) {
                    childList.add(new TreeNode(String.valueOf(guiId), o.toString()));
                    guiId++;
                }
                for (int i = rightExpression.size() - 1; i >= 0; i--) {
                    //EPSILON 不入栈
                    if (rightExpression.get(i) == TokenType.EPSILON) {
                        continue;
                    }
                    stk.push(rightExpression.get(i));
                    treeStack.push(childList.get(i));
                }
                node.setChildren(childList);
            }
            X = stk.peek();
            node = treeStack.peek();
        }
        System.out.println("错误列表为" + wrongList + "\n");
        syntaxTree = new SyntaxTree(root);
    }


    /**
     * 错误恢复
     */
    private void error(Object X, Token a) {
        System.out.println("--------error--------" + X + "  " + a);
        WrongMessage wrongMessage = null;
        if (X instanceof TokenType) {        // 如果是个终结符，就直接弹栈尝试继续分析
            stk.pop();
            wrongMessage = new WrongMessage(X.toString(), ErrorCode.MISSORADDMORE_SOMETHING, "语法分析阶段");
        } else if (X instanceof NonTerminalType) {      // 如果是个非终结符
            TreeSet<String> firstSetForX = firstSet.get(X.toString());
            TreeSet<String> followSetForX = followSet.get(X.toString());
            LLUtil llUtil = new LLUtil();
            HashMap<Pair<NonTerminalType, TokenType>, Object> parsingTable = llUtil.getParsingTable();
            boolean flag = false;
            for (Map.Entry<Pair<NonTerminalType, TokenType>, Object> entry : parsingTable.entrySet()) {
                if (entry.getKey().getKey() == X && entry.getKey().getValue() == a.getTokenType() && entry.getValue() == "synch") {
                    flag = true;    //如果矩阵元素为 synch，则弹出栈顶非终结符
                    System.out.println("wuhu");
                    stk.pop();
                }
            }
            if (!flag) {
                ip++;
                System.out.println("跳过当前输入符号"); // 指针前移
            }
            wrongMessage = new WrongMessage(X.toString(), ErrorCode.NOT_MATCH_FOR_GRAMMER, "语法分析阶段");
        }
        wrongList.put(new Pair<>(a.getRow(), a.getColumn()), wrongMessage);
    }


    public List<String> printParseTree() {
        Production p = productions.get(0);
        System.out.println("printParse " + p.getLeftExpression());
        List<String> res = recurseProduction(p);
        return res;
    }


    /**
     * 递归调用打印预测分析步骤
     *
     * @return
     */
    public List<String> recurseProduction(Production p) {
        List<Object> rightExpression = p.getRightExpression();
        ListIterator<Object> iterator = rightExpression.listIterator();
        List<String> res = new ArrayList<>();
        while (iterator.hasNext()) {
            Object s = iterator.next();
            if (s instanceof TokenType) {
                res.add(((TokenType) s).getValue());
                System.out.println(s);
                if (!iterator.hasNext()) {
                    return null;
                }
            } else if (s instanceof NonTerminalType) {
                res.add(((NonTerminalType) s).getValue());
                System.out.println(s);
                for (Production productionInfer : productions) {
                    if (productionInfer.getLeftExpression() == s && !productionInfer.getUsed()) {
                        productionInfer.setUsed();
                        recurseProduction(productionInfer);
                        break;
                    }
                }
            }
        }
        return res;
    }


    /**
     * 生成所有产生式的具体执行函数
     */
    public void createProduces(String nonTerminalType) {
        Set<Map.Entry<Integer, Production>> set = grammer.getProductions().entrySet();
        ArrayList<List<Object>> templist = new ArrayList<>();
        for (Map.Entry<Integer, Production> integerProductionEntry : set) {
            Production production = integerProductionEntry.getValue();
            NonTerminalType left_expr = production.getLeftExpression();
            List<Object> right_expr = production.getRightExpression();
            if (nonTerminalType == left_expr.getValue()) {
                templist.add(right_expr);
            }
        }
        productionMap.put(nonTerminalType, templist);
    }

    /**
     * 打印所有的产生式
     */
    public void printproductionMap() {
        for (Map.Entry<String, ArrayList<List<Object>>> entry : productionMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    @Deprecated
    // 输出语法树
    public void createParseTree() {
        Production p = productions.get(0);      // root 为 program
        TreeNode root = new TreeNode(p.getLeftExpression().getValue());
        List<Object> rightExpression = p.getRightExpression();
        treeStack.push(root);
        int i = 0;
        while (!treeStack.isEmpty()) {
            TreeNode node = treeStack.pop();
            Production p1 = productions.get(i++);
            List<Object> right = p1.getRightExpression();
            List<TreeNode> childList = new ArrayList<>();
            for (Object o : right) {
                childList.add(new TreeNode(o.toString()));
            }
            for (int j = right.size() - 1; j >= 0; j--) {
                if (right.get(j) instanceof NonTerminalType) {
                    treeStack.push(childList.get(j));
                }
            }
            node.setChildren(childList);
        }
        syntaxTree = new SyntaxTree(root);
    }


    //打印Gui用的语法树
    public GuiNode printGuiNode() {
        int guiId = 0;
        int j = 0;
        GuiNode root = new GuiNode(String.valueOf(guiId), "program");
        guiId++;
        Stack<GuiNode> leftMostStk = new Stack<>();
        leftMostStk.push(root);
        for (Production production : productions) {
            GuiNode curNode = leftMostStk.pop();
            List<Object> rightExpr = production.getRightExpression();
            List<GuiNode> children = new ArrayList<>();
            for (Object item : rightExpr) {
                if (item instanceof TokenType && (item == TokenType.NUM || item == TokenType.IDENTIFIERS)) {
                    children.add(new GuiNode(String.valueOf(guiId), item.toString() + "--" + values.get(j++)));
                } else {
                    children.add(new GuiNode(String.valueOf(guiId), item.toString()));
                }
                guiId++;
            }
            /* 非终结符倒序压栈 */
            for (int i = rightExpr.size() - 1; i >= 0; i--) {
                if (rightExpr.get(i) instanceof NonTerminalType) {
                    leftMostStk.push(children.get(i));
                }
            }
            /* child顺序入树 */
            for (int i = 0; i < rightExpr.size(); i++) {
                curNode.addChild(children.get(i));
            }
        }
        return root;
    }
}
