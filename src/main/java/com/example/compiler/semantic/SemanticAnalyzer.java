package com.example.compiler.semantic;

import com.example.compiler.entity.token.TokenType;
import com.example.compiler.entity.tree.SyntaxTree;
import com.example.compiler.entity.wrong.WrongMessage;
import com.example.compiler.lexer.Lexer;
import com.example.compiler.llParser.LLParser;
import com.example.compiler.llParser.NonTerminalType;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName SemanticAnalyzer.java
 * @Description TODO
 * @createTime 2021年06月04日 17:30:00
 */
@SuppressWarnings("all")
public class SemanticAnalyzer {
    private SymbolTable symbolTable;        // 全局变量符号表
    private Lexer lexer;                    // 调用的词法分析器
    private int position = 0;               // 语法树列表的当前位置
    private final HashMap<String, String> assistMap = new HashMap<>();
    private ThreeCodeTable threeCodeTable;  // 三地址符号表
    private final HashMap<Pair<Integer, Integer>, WrongMessage> wrongList = new HashMap<>();    //错误列表
    private SyntaxTree syntaxTree;          // 语法树
    private List<String> productionList;    // 语法树 ——> 语法树列表
    private int queuePos = 0;               // 辅助队列位置
    private int label = 0;                  // 生成三地址码的代码块标号
    private int v = 1;                      // 生成三地址码的临时变量标号
    private int id = 0;                     // 生成三地址码的标识符标号
    private LLParser llParser;              // 调用的语法分析器
    private boolean while_loop = false;     // 判断是否是while循环（防止中间代码多次输出）
    private int cotrol_while_cnt = 0;
    private List<String> intermediateCodeList = new ArrayList<>();  // 中间代码结果

    public List<String> getIntermediateCodeList() {
        return intermediateCodeList;
    }

    public String PrintNewBlock() {
        String s = "LABEL  label" + (++this.label);
        return s;
    }

    public String PrintNewBlock(int i) {
        String s = "LABEL  label" + (this.label + i);
        return s;
    }

    public String PrintNewSymbol() {
        String s = "v" + (this.v);
        return s;
    }

    public String PrintNewSymbol2(int i) {
        String s = "v" + (this.v) + i;
        return s;
    }

    public String PrintNewId() {
        String s = "id" + (++this.id);
        return s;
    }

    public SemanticAnalyzer(String fileContent) throws Exception {
        lexer = new Lexer(fileContent);
        llParser = new LLParser(fileContent);
        init();
        if (productionList.get(position) == "program") {
            position++;
            program(true);
        } else
            throw new Exception("程序异常");
    }

    public void init() {
        symbolTable = new SymbolTable();
        threeCodeTable = new ThreeCodeTable();
        syntaxTree = llParser.getSyntaxTree();
        productionList = llParser.getSyntaxTree().dfs();
    }

    @Override
    public String toString() {
        return symbolTable.toString();
    }

    public HashMap<String, SymbolTable.Variable> getSymbolTable() {
        return symbolTable.getTableMap();
    }

    public void program(boolean expr) throws Exception {
        if (productionList.get(position) == "compoundstmt") {
            System.out.println(PrintNewBlock());
            intermediateCodeList.add(PrintNewBlock(-1));
            position++;
            compoundstmt(true);
        }
    }

    public void compoundstmt(boolean expr) throws Exception {
        String temp = productionList.get(position);
        if (temp.equals(TokenType.OPENCURLYBRACE.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.STMTS.getValue())) {
                position++;
                stmts(expr);
                if (!productionList.get(position).equals(TokenType.CLOSECURLYBRACE.getValue()))
                    throw new Exception("程序未闭合");
            }
        }
    }

    public void stmts(boolean expr) throws Exception {
        String temp = productionList.get(position);
        if (temp.equals(NonTerminalType.STMT.getValue())) {
            position++;
            stmt(expr);
            if (productionList.get(position) == NonTerminalType.STMTS.toString()) {
                position++;
                stmts((expr));
            }
        } else if (temp.equals(TokenType.EPSILON.getValue())) {
            position++;
        }
    }

    public void stmt(boolean expr) throws Exception {
        String temp = productionList.get(position);
        if (temp.equals(NonTerminalType.COMPOUNDSTMT.getValue())) {
            position++;
            compoundstmt(expr);
        } else if (temp.equals(NonTerminalType.IFSTMT.getValue())) {
            position++;
            ifstmt(expr);
        } else if (temp.contains(NonTerminalType.ASSGSTMT.getValue())) {
            position++;
            assgstmt(expr);
        } else if (temp.equals(NonTerminalType.WHILESTMT.getValue())) {
            position++;
            whilestmt(expr);
        }
    }

    public void ifstmt(boolean expr) throws Exception {
        String temp = productionList.get(position);
        if (temp.equals(TokenType.IF.getValue())) {
            position++;
            if (productionList.get(position).equals(TokenType.OPENBRACE.getValue())) {
                position++;
                if (productionList.get(position).equals(NonTerminalType.BOOLEXPR.getValue())) {
                    position++;
                    Boolean boolsyn = boolexpr();
                    if (productionList.get(position).equals(TokenType.CLOSEBRACE.getValue())) {
                        position++;
                        if (productionList.get(position).contains(TokenType.THEN.getValue())) {
                            if (!while_loop) {
                                System.out.println(PrintNewBlock());
                                intermediateCodeList.add(PrintNewBlock(-1));
                            }
                            position++;
                            if (productionList.get(position).equals(NonTerminalType.STMT.getValue())) {
                                position++;
                                stmt(expr && boolsyn != null && boolsyn);
                                if (productionList.get(position).contains(TokenType.ELSE.getValue())) {
                                    if (!while_loop) {
                                        System.out.println(PrintNewBlock());
                                        intermediateCodeList.add(PrintNewBlock(-1));
                                    }
                                    position++;
                                    if (productionList.get(position).equals(NonTerminalType.STMT.getValue())) {
                                        position++;
                                        stmt(expr && boolsyn != null && !boolsyn);
                                        position++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Boolean boolexpr() throws Exception {
        String temp = productionList.get(position);
        Boolean syn = null;
        if (temp.equals(NonTerminalType.ARITHEXPR.getValue())) {
            position++;
            Pair<String, Number> arithexprSyn = arithexpr();                                                      // arithexpr1.inh = arithexpr.syn
            if (productionList.get(position).equals(NonTerminalType.BOOLOP.getValue())) {
                position++;
                String op = boolop();                                                                   // arithexpr1.op = boolop.op
                if (productionList.get(position).equals(NonTerminalType.ARITHEXPR.getValue())) {
                    position++;
                    syn = arithexpr(arithexprSyn, op);
                }
            }
        }
        return syn;
    }

    private String boolop() throws Exception {
        String temp = productionList.get(position);
        String syn = null;
        // match4Ex2Grammar the first set
        if (temp.contains(TokenType.LESS.getValue()) || temp.contains(TokenType.LESSEQUAL.getValue()) || temp.contains(TokenType.GREATER.getValue()) || temp.contains(TokenType.GREATEREQUAL.getValue()) || temp.contains(TokenType.EQUALEQUAL.getValue())) {
            String[] tempList = temp.split(":");
            syn = tempList[0]; // boolop.op = token.val
            position++;
        }
        return syn;
    }

    /**
     * @param inh boolop左侧的数值
     * @param op  boolop的运算符类型
     * @return boolexpr的值，true/false
     */
    private Boolean arithexpr(Pair<String, Number> inh, String op) throws Exception {
        Pair<String, Number> syn = arithexpr();              // 先计算出boolop右侧的值
        if (!while_loop) {
            if (assistMap.get(syn.getKey()) == null) {
                intermediateCodeList.add("if " + assistMap.get(inh.getKey()) + " " + op + " " + (syn.getKey()) + " goto L" + (this.label + 1));
                System.out.println("if " + assistMap.get(inh.getKey()) + " " + op + " " + (syn.getKey()) + " goto L" + (this.label + 1));
            } else {
                intermediateCodeList.add("if " + assistMap.get(inh.getKey()) + " " + op + " " + assistMap.get(syn.getKey()) + " goto L" + (this.label + 1));
                System.out.println("if " + assistMap.get(inh.getKey()) + " " + op + " " + assistMap.get(syn.getKey()) + " goto L" + (this.label + 1));
            }
        }
        switch (op) {                          // 根据不同的运算符类型执行不同的bool操作
            case "<":
                if (inh.getValue() instanceof Integer && syn.getValue() instanceof Integer)    // 自动类型转换，只有左值和右值均为int类型时，才按照int类型比较
                    return inh.getValue().intValue() < syn.getValue().intValue();
                else
                    return inh.getValue().doubleValue() < syn.getValue().doubleValue();
            case "<=":
                if (inh.getValue() instanceof Integer && syn.getValue() instanceof Integer)
                    return inh.getValue().intValue() <= syn.getValue().intValue();
                else
                    return inh.getValue().doubleValue() <= syn.getValue().doubleValue();
            case ">":
                if (inh.getValue() instanceof Integer && syn.getValue() instanceof Integer)
                    return inh.getValue().intValue() > syn.getValue().intValue();
                else
                    return inh.getValue().doubleValue() > syn.getValue().doubleValue();
            case ">=":
                if (inh.getValue() instanceof Integer && syn.getValue() instanceof Integer)
                    return inh.getValue().intValue() >= syn.getValue().intValue();
                else
                    return inh.getValue().doubleValue() >= syn.getValue().doubleValue();
            case "==":
                if (inh.getValue() instanceof Integer && syn.getValue() instanceof Integer)
                    return inh.getValue().intValue() == syn.getValue().intValue();
                else
                    return inh.getValue().doubleValue() == syn.getValue().doubleValue();
            default:
                break;
        }
        return null;
    }

    public void whilestmt(boolean expr) throws Exception {
        int i = 0;
        int temp_pos = 0;
        int temp_end = 0;
        String temp = productionList.get(position);
        if (temp.equals(TokenType.WHILE.getValue())) {
            System.out.println(PrintNewBlock());
            intermediateCodeList.add(PrintNewBlock(-1));
            position++;
            boolean condition;
            if (productionList.get(position).equals(TokenType.OPENBRACE.getValue())) {
                position++;
                if (productionList.get(position).equals(NonTerminalType.BOOLEXPR.getValue())) {
                    position++;
                    temp_pos = position;
                    while (true) {
                        i++;
                        position = temp_pos;
                        Boolean boolsyn = boolexpr();
                        condition = expr && boolsyn != null && boolsyn;
                        if (productionList.get(position).equals(TokenType.CLOSEBRACE.getValue())) {
                            position++;
                            if (!condition) {
                                position = temp_end + 1;
                                break;
                            }
                            if (productionList.get(position).equals(NonTerminalType.STMT.getValue())) {
                                position++;
                                stmt(condition);
                                temp_end = position;
                                while_loop = true;
                            }
                        }
                    }
                    while_loop = false;
                }
            }
        }
    }

    public void assgstmt(boolean expr) throws Exception {
        String temp = productionList.get(position);
        String[] seperateList = temp.split(":");
        String name = seperateList[1];
        if (temp.contains(TokenType.IDENTIFIERS.getValue())) {
            position++;
            if (productionList.get(position).equals(TokenType.EQUAL.getValue())) {
                position++;
                if (productionList.get(position).equals(NonTerminalType.ARITHEXPR.getValue())) {
                    position++;
                    Pair<String, Number> syn = arithexpr();
                    if (productionList.get(position).equals(TokenType.SEMICOLON.getValue())) {
                        position++;
                        if (expr)
                            symbolTable.putSingleSymbol(name, syn.getValue());
                        String right;
                        if (assistMap.containsKey(name)) right = assistMap.get(name);
                        else right = PrintNewId();
                        if (!while_loop) {
                            if (symbolTable.getVal(syn.getKey()).intValue() != Integer.MAX_VALUE) {         // 如果是形如 a = i这样的赋值
                                intermediateCodeList.add(right + " := " + assistMap.get(syn.getKey()));
                                System.out.println(right + " := " + assistMap.get(syn.getKey()));
                            } else {
                                // 如果是形如 a = 5这样的赋值
                                this.v--;
                                System.out.println(right + " := " + PrintNewSymbol());
                                intermediateCodeList.add(right + " := " + PrintNewSymbol());
                                this.v++;
                            }
                        }
                        assistMap.put(name, right);
                    }
                }
            }
        }
    }

    public Pair<String, Number> arithexpr() throws Exception {
        String temp = productionList.get(position);
        Pair<String, Number> syn = null;
        if (temp.equals(NonTerminalType.MULTEXPR.getValue())) {
            position++;
            Pair<String, Number> multexprSyn = multexpr();
            if (productionList.get(position).equals(NonTerminalType.ARITHEXPRPRIME.getValue())) {
                position++;
                syn = arithexprprime(multexprSyn);
            }
        }
        return syn;
    }

    public Pair<String, Number> multexpr() throws Exception {
        String temp = productionList.get(position);
        Pair<String, Number> syn = null;
        if (temp.equals(NonTerminalType.SIMPLEEXPR.getValue())) {
            position++;
            Pair<String, Number> simpleexprSyn = simpleexpr();         // multexprprime.inh = simpleexpr.syn
            if (productionList.get(position) == NonTerminalType.MULTEXPRPRIME.getValue()) {
                position++;
                syn = multexprprime(simpleexprSyn);// multexpr.syn = multexprprime.syn
            }
        }
        return syn;
    }

    public Pair<String, Number> simpleexpr() throws Exception {
        String temp = productionList.get(position);
        Pair<String, Number> syn = null;
        Number syn1 = null;
        if (temp.equals(TokenType.OPENBRACE.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.ARITHEXPR.getValue())) {
                position++;
                syn = arithexpr();
                if (productionList.get(position).equals(TokenType.CLOSEBRACE.getValue())) {
                    position++;
                }
            }
        } else if (temp.contains(TokenType.IDENTIFIERS.getValue())) {
            String[] seperateList = temp.split(":");
            String name = seperateList[1];
            syn = new Pair<>(name, symbolTable.getVal(name));
            position++;
        } else if (temp.contains(TokenType.NUM.getValue())) {
            String[] seperateList = temp.split(":");
            String val = seperateList[1];
            if (val.indexOf('.') == -1)  //查不到字符'.'，返回-1。另外注意是单引号。
                syn1 = Integer.parseInt(val);
            else
                syn1 = Double.parseDouble(val);
            String left = PrintNewSymbol();
            syn = new Pair<>(left, syn1);
            if (!while_loop) {
                intermediateCodeList.add(left + " := #" + syn1);
                System.out.println(left + " := #" + syn1);
                this.v++;
            }
            position++;
        }
        return syn;
    }

    public Pair<String, Number> multexprprime(Pair<String, Number> inh) throws Exception {
        String temp = productionList.get(position);
        Pair<String, Number> syn = inh;
        if (temp.equals(TokenType.MULTIPLY.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.SIMPLEEXPR.getValue())) {
                position++;
                Pair<String, Number> simpleexprSyn = simpleexpr();
                if (simpleexprSyn == null)
                    throw new Exception("multexprprime解析出错1");
                if (productionList.get(position).equals(NonTerminalType.MULTEXPRPRIME.getValue())) {
                    position++;
                    String left = PrintNewSymbol();// multexprprime.syn = arithexprprime1.syn
                    String arg1, arg2 = null;
                    if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE) && symbolTable.getVal(simpleexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                        arg1 = inh.getKey() + "";
                        arg2 = simpleexprSyn.getKey() + "";
                    } else {
                        if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE)) {
                            arg1 = inh.getKey() + "";
                        } else {
                            arg1 = assistMap.get(inh.getKey());
                        }
                        if (symbolTable.getVal(simpleexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                            arg2 = simpleexprSyn.getKey() + "";
                        } else {
                            arg2 = assistMap.get(simpleexprSyn.getKey());
                        }
                    }
                    if (inh.getValue() instanceof Integer && simpleexprSyn.getValue() instanceof Integer)             // arithexprprime1.inh = multexprprime.inh * simpleexpr.syn
                    {
                        syn = multexprprime(new Pair<>(left, inh.getValue().intValue() * simpleexprSyn.getValue().intValue()));
                    } else {
                        syn = multexprprime(new Pair<>(left, inh.getValue().doubleValue() * simpleexprSyn.getValue().doubleValue()));
                    }
                    if (!while_loop) {
                        intermediateCodeList.add(left + " := " + arg1 + " * " + arg2);
                        System.out.println(left + " := " + arg1 + " * " + arg2);
                        this.v++;
                    }
                }
            }
        } else if (temp.equals(TokenType.DIVIDE.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.SIMPLEEXPR.getValue())) {
                position++;
                Pair<String, Number> simpleexprSyn = simpleexpr();
                if (productionList.get(position).equals(NonTerminalType.MULTEXPRPRIME.getValue())) {
                    position++;
                    String left = PrintNewSymbol();// multexprprime.syn = arithexprprime1.syn
                    String arg1, arg2 = null;
                    if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE) && symbolTable.getVal(simpleexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                        arg1 = inh.getKey() + "";
                        arg2 = simpleexprSyn.getKey() + "";
                    } else {
                        if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE)) {
                            arg1 = inh.getKey() + "";
                        } else {
                            arg1 = assistMap.get(inh.getKey());
                        }
                        if (symbolTable.getVal(simpleexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                            arg2 = simpleexprSyn.getKey() + "";
                        } else {
                            arg2 = assistMap.get(simpleexprSyn.getKey());
                        }
                    }
                    if (inh.getValue() instanceof Integer && simpleexprSyn.getValue() instanceof Integer)             // arithexprprime1.inh = multexprprime.inh * simpleexpr.syn
                    {
                        syn = multexprprime(new Pair<>(left, inh.getValue().intValue() / simpleexprSyn.getValue().intValue()));
                    } else {
                        syn = multexprprime(new Pair<>(left, inh.getValue().doubleValue() / simpleexprSyn.getValue().doubleValue()));
                    }
                    if (!while_loop) {
                        intermediateCodeList.add(left + " := " + arg1 + " / " + arg2);
                        System.out.println(left + " := " + arg1 + " / " + arg2);
                        this.v++;
                    }
                }
            }
        } else if (productionList.get(position).equals(TokenType.EPSILON.getValue())) {
            position++;
        }
        return syn;
    }

    public Pair<String, Number> arithexprprime(Pair<String, Number> inh) throws Exception {
        String temp = productionList.get(position);
        Pair<String, Number> syn = inh;
        if (temp.equals(TokenType.PLUS.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.MULTEXPR.getValue())) {
                position++;
                Pair<String, Number> multexprSyn = multexpr();
                if (productionList.get(position).equals(NonTerminalType.ARITHEXPRPRIME.getValue())) {
                    position++;
                    String left = PrintNewSymbol();// multexprprime.syn = arithexprprime1.syn
                    String arg1, arg2 = null;
                    if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE) && symbolTable.getVal(multexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                        arg1 = inh.getKey() + "";
                        arg2 = multexprSyn.getKey() + "";
                    } else {
                        if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE)) {
                            arg1 = inh.getKey() + "";
                        } else {
                            arg1 = assistMap.get(inh.getKey());
                        }
                        if (symbolTable.getVal(multexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                            arg2 = multexprSyn.getKey() + "";
                        } else {
                            arg2 = assistMap.get(multexprSyn.getKey());
                        }
                    }
                    if (inh.getValue() instanceof Integer && multexprSyn.getValue() instanceof Integer)             // arithexprprime1.inh = multexprprime.inh + simpleexpr.syn
                    {
                        syn = arithexprprime(new Pair<>(left, inh.getValue().intValue() + multexprSyn.getValue().intValue()));    // multexprprime.syn = arithexprprime1.syn
                    } else {
                        syn = arithexprprime(new Pair<>(left, inh.getValue().doubleValue() + multexprSyn.getValue().doubleValue()));
                    }
                    if (!while_loop) {
                        intermediateCodeList.add(left + " := " + arg1 + " + " + arg2);
                        System.out.println(left + " := " + arg1 + " + " + arg2);
                        this.v++;
                    }
                }
            }

        } else if (temp.equals(TokenType.MINUS.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.MULTEXPR.getValue())) {
                position++;
                Pair<String, Number> multexprSyn = multexpr();
                if (productionList.get(position).equals(NonTerminalType.ARITHEXPRPRIME.getValue())) {
                    position++;
                    String left = PrintNewSymbol();// multexprprime.syn = arithexprprime1.syn
                    String arg1, arg2 = null;
                    if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE) && symbolTable.getVal(multexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                        arg1 = inh.getKey() + "";
                        arg2 = multexprSyn.getKey() + "";
                    } else {
                        if (symbolTable.getVal(inh.getKey()).equals(Integer.MAX_VALUE)) {
                            arg1 = inh.getKey() + "";
                        } else {
                            arg1 = assistMap.get(inh.getKey());
                        }
                        if (symbolTable.getVal(multexprSyn.getKey()).equals(Integer.MAX_VALUE)) {
                            arg2 = multexprSyn.getKey() + "";
                        } else {
                            arg2 = assistMap.get(multexprSyn.getKey());
                        }
                    }
                    if (inh.getValue() instanceof Integer && multexprSyn.getValue() instanceof Integer)             // arithexprprime1.inh = multexprprime.inh + simpleexpr.syn
                    {
                        syn = arithexprprime(new Pair<>(left, inh.getValue().intValue() - multexprSyn.getValue().intValue()));    // multexprprime.syn = arithexprprime1.syn
                    } else {
                        syn = arithexprprime(new Pair<>(left, inh.getValue().doubleValue() - multexprSyn.getValue().doubleValue()));
                    }
                    if (!while_loop) {
                        intermediateCodeList.add(left + " := " + arg1 + " - " + arg2);
                        System.out.println(left + " := " + arg1 + " - " + arg2);
                        this.v++;
                    }
                }
            }
        } else if (productionList.get(position).equals(TokenType.EPSILON.getValue())) {
            position++;
        }
        return syn;
    }
}
