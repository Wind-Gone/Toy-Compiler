package com.example.compiler.llParser;

import com.example.compiler.entity.gui.GuiNode;
import com.example.compiler.entity.token.Token;
import com.example.compiler.entity.token.TokenType;
import com.example.compiler.entity.tree.SyntaxTree;
import com.example.compiler.entity.tree.TreeNode;
import com.example.compiler.entity.wrong.ErrorCode;
import com.example.compiler.entity.wrong.WrongMessage;
import com.example.compiler.lexer.Lexer;
import javafx.util.Pair;

import java.util.*;

@SuppressWarnings("all")
public class LLParser {
    private final Grammer grammer;
    private final ParsingTable parsingTable;
    private final List<Production> productions;                         // 存储每个非终结符的所有产生式
    private final HashMap<Pair<Integer, Integer>, WrongMessage> wrongList = new HashMap<>();    //错误列表
    private String[][] finalTable;              //最终的打印表
    private static String start = "PROGRAM";    // 起始符号
    private List<Token> w;              // 分析程序的输入串
    private Stack<Object> stk;          // 分析程序的栈
    private int ip = 0;                 // 输入串顶指针
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
        stk_init();
        w_init(input);
        lmDerivation();
        createParseTree();
    }

    private void stk_init() {
        stk = new Stack<>();
        stk.push(TokenType.DOLLAR);
        stk.push(NonTerminalType.PROGRAM);
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
     * 输出最左推导 -- 书上的伪代码
     */
    private void lmDerivation() {
        ip = 0;
        Object X = stk.peek();
        Token current = null;
        while (X != TokenType.DOLLAR) {
            System.out.println("------stk-------------: " + stk);
            if (ip >= w.size())
                break;
            System.out.println("------token-----------: " + w.get(ip).getTokenType());
            System.out.println();
            TokenType a = w.get(ip).getTokenType();
            Token b = w.get(ip);
            current = w.get(ip);
            if (X == a) {                      // X是终结符且匹配成功
                stk.pop();
                /** 如果X是nums或者id ， 把值加入values ， 打印 GUITree用 */
                if (b.getTokenType() == TokenType.NUM || b.getTokenType() == TokenType.IDENTIFIERS) {
                    values.add(b.getTokenString());
                }
                ip++;
            } else if (X instanceof TokenType) {                                // X是终结符且出错
                error(X, current);
            } else if (parsingTable.get((NonTerminalType) X, a) == -1) {            // X是非终结符且出错
                error(X, current);
            } else {
                Production production = grammer.get(parsingTable.get((NonTerminalType) X, a));
                productions.add(production);
                stk.pop();
                List<Object> rightExpression = production.getRightExpression();
                for (int i = rightExpression.size() - 1; i >= 0; i--) {
                    //EPSILON 不入栈
                    if (rightExpression.get(i) == TokenType.EPSILON) {
                        continue;
                    }
                    stk.push(rightExpression.get(i));
                }
            }
            X = stk.peek();
        }
        System.out.println("错误列表为" + wrongList + "\n");
//        syntaxTree = new SyntaxTree(root);
    }


    /**
     * 错误恢复
     */
    private void error(Object X, Token a) {
        System.out.println("--------error--------" + a);
        if (a.getTokenString() == null && stk.size() == 3) {
            w.add(w.size() - 1, new Token(TokenType.CLOSECURLYBRACE));
            WrongMessage wrongMessage = new WrongMessage("}", ErrorCode.MISS_END_CLOSECURLYBRACES, "语法阶段");
            wrongList.put(new Pair<>(a.getRow(), a.getColumn()), wrongMessage);
        } else {

            WrongMessage wrongMessage = null;
            if (X instanceof TokenType) {        // 如果是个终结符，就直接弹栈尝试继续分析
                stk.pop();
                wrongMessage = new WrongMessage(a.toString(), ErrorCode.MISS_END_CLOSECURLYBRACES, "语法分析阶段");
            } else if (X instanceof NonTerminalType) {      // 如果是个非终结符
                if (((NonTerminalType) X).equals(NonTerminalType.PROGRAM)) {                // 特判处理开始符号不是‘{’的情况
                    if (!a.getTokenString().equals("{")) {
                        wrongMessage = new WrongMessage("{", ErrorCode.MISS_START_OPENCURLYBRACES, "语法分析阶段");  // 短语层次恢复
                        w.add(0, new Token(TokenType.OPENCURLYBRACE));
                    }
                } else {
                    LLUtil llUtil = new LLUtil();
                    HashMap<Pair<NonTerminalType, TokenType>, Object> parsingTable = llUtil.getParsingTable();
                    boolean flag = false;
                    for (Map.Entry<Pair<NonTerminalType, TokenType>, Object> entry : parsingTable.entrySet()) {
                        if (entry.getKey().getKey() == X && entry.getKey().getValue() == a.getTokenType() && entry.getValue() == "synch") {
                            flag = true;    //如果矩阵元素为 synch，则弹出栈顶非终结符
                            stk.pop();
                        }
                    }
                    if (!flag) {
                        ip++;
                        System.out.println("跳过当前输入符号"); // 指针前移
                    }
                    if (a.getTokenType().equals(TokenType.IDENTIFIERS))
                        wrongMessage = new WrongMessage(a.toString(), ErrorCode.EXTRA_VARIABLE_USE, "语法分析阶段");
                    else if (a.getTokenType().equals(TokenType.CLOSEBRACE))
                        wrongMessage = new WrongMessage(a.toString(), ErrorCode.MISS_OR_EXTRA_OPENBRACE, "语法分析阶段");
                    else if (a.getTokenType().equals(TokenType.SEMICOLON))
                        wrongMessage = new WrongMessage(a.toString(), ErrorCode.EXTRA_SEMICOLON, "语法分析阶段");
                    else
                        wrongMessage = new WrongMessage(a.toString(), ErrorCode.WRONG_GRAMMER_PARSER, "语法分析阶段");
                }
            }
            wrongList.put(new Pair<>(a.getRow(), a.getColumn()), wrongMessage);
        }
    }

    // 输出语法树
    public void createParseTree() {
        Production p = productions.get(0);      // root 为 program
        TreeNode root = new TreeNode(p.getLeftExpression().getValue());
        List<Object> rightExpression = p.getRightExpression();
        treeStack.push(root);
        int pointer = 0;
        int i = 0;
        while (!treeStack.isEmpty()) {
            TreeNode node = treeStack.pop();
            if (i >= productions.size())
                break;
            Production p1 = productions.get(i++);
            List<Object> right = p1.getRightExpression();
            List<TreeNode> childList = new ArrayList<>();
            for (Object o : right) {
                if (o instanceof TokenType && (o == TokenType.NUM || o == TokenType.IDENTIFIERS)) {
                    childList.add(new TreeNode(o.toString() + ":" + values.get(pointer++)));
                } else {
                    childList.add(new TreeNode(o.toString()));
                }
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
