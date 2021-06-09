package com.example.compiler.semantic;

import com.example.compiler.entity.token.TokenType;
import com.example.compiler.entity.tree.SyntaxTree;
import com.example.compiler.entity.wrong.WrongMessage;
import com.example.compiler.lexer.Lexer;
import com.example.compiler.llParser.LLParser;
import com.example.compiler.llParser.NonTerminalType;
import javafx.util.Pair;

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
    //    private ThreeCodeTable threeCodeTable;  // 三地址符号表
//    private int queuePos = 0;               // 辅助队列位置
    private final HashMap<Pair<Integer, Integer>, WrongMessage> wrongList = new HashMap<>();    //错误列表
    private SyntaxTree syntaxTree;          // 语法树
    private List<String> productionList;    // 语法树 ——> 语法树列表
    //    private int label = 0;                  // 生成三地址码的代码块标号
//    private int v = 0;                      // 生成三地址码的临时变量标号
//    private int id = 0;                     // 生成三地址码的标识符标号
//    private Queue<String> StoreQueue;       //生成三地址码的辅助队列
    private LLParser llParser;              // 调用的语法分析器

//    public String PrintNewBlock() {
//        String s = "label" + (++this.label);
//        StoreQueue.offer(s);
//        return s;
//    }
//
//    public String PrintNewSymbol() {
//        String s = "v" + (++this.v);
//        StoreQueue.offer(s);
//        return s;
//    }
//
//    public String PrintNewId() {
//        String s = "id" + (++this.id);
//        StoreQueue.offer(s);
//        return s;
//    }

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
//        threeCodeTable = new ThreeCodeTable();
//        StoreQueue = new LinkedList<String>();
        syntaxTree = llParser.getSyntaxTree();
        productionList = llParser.getSyntaxTree().dfs();
    }

    @Override
    public String toString() {
        return symbolTable.toString();
    }

    public void program(boolean expr) throws Exception {
        if (productionList.get(position) == "compoundstmt") {
            position++;
            compoundstmt(true);
        } else
            throw new Exception("程序异常2");
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
            } else throw new Exception("stmt报错");
        } else
            throw new Exception("程序缺失左花括号");
    }

    public void stmts(boolean expr) throws Exception {
        String temp = productionList.get(position);
        if (temp.equals(NonTerminalType.STMT.getValue())) {
            position++;
            stmt(expr);
            if (productionList.get(position) == NonTerminalType.STMTS.toString()) {
                position++;
                stmts((expr));
            } else throw new Exception("stmts STMTS报错");
        } else if (temp.equals(TokenType.EPSILON.getValue())) {
            position++;
        } else throw new Exception("stmts expr报错");
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
        } else throw new Exception("stmt 不匹配");
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
                            position++;
                            if (productionList.get(position).equals(NonTerminalType.STMT.getValue())) {
                                position++;
                                stmt(expr && boolsyn != null && boolsyn);
//                                position++;
                                if (productionList.get(position).contains(TokenType.ELSE.getValue())) {
                                    position++;
                                    if (productionList.get(position).equals(NonTerminalType.STMT.getValue())) {
                                        position++;
                                        stmt(expr && boolsyn != null && !boolsyn);
                                        position++;
                                    } else throw new Exception("stmt if 报错2");
                                } else throw new Exception("ELSE 语句 报错");
                            } else throw new Exception("stmt if 报错1");
                        } else throw new Exception("THEN语句报错");
                    } else throw new Exception("if语句没有右括号");
                } else throw new Exception("BOOLEXPR报错");
            } else throw new Exception("if语句没有左括号");
        } else throw new Exception("if语句报错");
    }

    private Boolean boolexpr() throws Exception {
        String temp = productionList.get(position);
        Boolean syn = null;
        if (temp.equals(NonTerminalType.ARITHEXPR.getValue())) {
            position++;
            Number arithexprSyn = arithexpr();                                                      // arithexpr1.inh = arithexpr.syn
            if (productionList.get(position).equals(NonTerminalType.BOOLOP.getValue())) {
                position++;
                String op = boolop();                                                                   // arithexpr1.op = boolop.op
                if (productionList.get(position).equals(NonTerminalType.ARITHEXPR.getValue())) {
                    position++;
                    syn = arithexpr(arithexprSyn, op);
                } else throw new Exception("boolexpr ARITHEXPR报错");
            } else
                throw new Exception("boolexpr BOOLOP报错");                                                  // boolexpr.syn = arithexpr1.syn
        } else throw new Exception("boolexpr ARITHEXPRPRIME报错");
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
        } else throw new Exception("boolop 条件符号报错");
        return syn;
    }

    /**
     * @param inh boolop左侧的数值
     * @param op  boolop的运算符类型
     * @return boolexpr的值，true/false
     */
    private Boolean arithexpr(Number inh, String op) throws Exception {
        Number syn = arithexpr();              // 先计算出boolop右侧的值
        switch (op) {                          // 根据不同的运算符类型执行不同的bool操作
            case "<":
                if (inh instanceof Integer && syn instanceof Integer)    // 自动类型转换，只有左值和右值均为int类型时，才按照int类型比较
                    return inh.intValue() < syn.intValue();
                else
                    return inh.doubleValue() < syn.doubleValue();
            case "<=":
                if (inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() <= syn.intValue();
                else
                    return inh.doubleValue() <= syn.doubleValue();
            case ">":
                if (inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() > syn.intValue();
                else
                    return inh.doubleValue() > syn.doubleValue();
            case ">=":
                if (inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() >= syn.intValue();
                else
                    return inh.doubleValue() >= syn.doubleValue();
            case "==":
                if (inh instanceof Integer && syn instanceof Integer)
                    return inh.intValue() == syn.intValue();
                else
                    return inh.doubleValue() == syn.doubleValue();
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

                            } else throw new Exception("while语句中stmt报错");
                        } else throw new Exception("while语句没有右括号");
                    }
                } else throw new Exception("While语句包含非法的语句判断");
            } else throw new Exception("While语句没有左括号");
        } else throw new Exception("whilestmt While符号不正确");
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
                    Number syn = arithexpr();
                    if (productionList.get(position).equals(TokenType.SEMICOLON.getValue())) {
                        position++;
                        if (expr) {
                            symbolTable.putSingleSymbol(name, syn);
//                            String right = PrintNewId();
//                            System.out.println(right + "=" + StoreQueue.poll());
                        }
                    } else
                        throw new Exception("赋值语句没有分号");
                } else
                    throw new Exception("ARITHEXPR报错");
            } else
                throw new Exception("非法的赋值语句");
        } else
            throw new Exception("assgstmt 报错");
    }

    public Number arithexpr() throws Exception {
        String temp = productionList.get(position);
        Number syn = null;
        if (temp.equals(NonTerminalType.MULTEXPR.getValue())) {
            position++;
            Number multexprSyn = multexpr();
            if (productionList.get(position).equals(NonTerminalType.ARITHEXPRPRIME.getValue())) {
                position++;
                syn = arithexprprime(multexprSyn);
            } else
                throw new Exception("arithexprprime报错");
        } else throw new Exception("arithexpr MULTEXPR 报错");
        return syn;
    }

    public Number multexpr() throws Exception {
        String temp = productionList.get(position);
        Number syn = null;
        if (temp.equals(NonTerminalType.SIMPLEEXPR.getValue())) {
            position++;
            Number simpleexprSyn = simpleexpr();         // multexprprime.inh = simpleexpr.syn
            if (productionList.get(position) == NonTerminalType.MULTEXPRPRIME.getValue()) {
                position++;
                syn = multexprprime(simpleexprSyn);// multexpr.syn = multexprprime.syn
            } else throw new Exception("multexpr MULTEXPRPRIME 报错");
        } else throw new Exception("multexpr SIMPLEEXPR报错");
        return syn;
    }

    public Number simpleexpr() throws Exception {
        String temp = productionList.get(position);
        Number syn = null;
        if (temp.equals(TokenType.OPENBRACE.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.ARITHEXPR.getValue())) {
                position++;
                syn = arithexpr();
                if (productionList.get(position).equals(TokenType.CLOSEBRACE.getValue())) {
                    position++;
                } else throw new Exception("右括号未闭合");
            } else throw new Exception("simpleexpr arithexpr报错");
        } else if (temp.contains(TokenType.IDENTIFIERS.getValue())) {
            String[] seperateList = temp.split(":");
            String name = seperateList[1];
            syn = symbolTable.getVal(name);
            position++;
        } else if (temp.contains(TokenType.NUM.getValue())) {
            String[] seperateList = temp.split(":");
            String val = seperateList[1];
            if (val.indexOf('.') == -1)  //查不到字符'.'，返回-1。另外注意是单引号。
                syn = Integer.parseInt(val);
            else
                syn = Double.parseDouble(val);
            position++;
        }
        return syn;
    }

    public Number multexprprime(Number inh) throws Exception {
        String temp = productionList.get(position);
        Number syn = inh;
        if (temp.equals(TokenType.MULTIPLY.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.SIMPLEEXPR.getValue())) {
                position++;
                Number simpleexprSyn = simpleexpr();
                if (simpleexprSyn == null)
                    throw new Exception("multexprprime解析出错1");
                if (productionList.get(position).equals(NonTerminalType.MULTEXPRPRIME.getValue())) {
                    position++;
//                    String left = PrintNewSymbol();
//                    if (!StoreQueue.peek().equals(left))
//                        System.out.println(left + "=" + inh.intValue() + "*" + StoreQueue.poll());
//                    else
//                        System.out.println(left + "=" + inh.intValue() + "*" + simpleexprSyn);
                    if (inh instanceof Integer && simpleexprSyn instanceof Integer)             // arithexprprime1.inh = multexprprime.inh * simpleexpr.syn
                        syn = multexprprime(inh.intValue() * simpleexprSyn.intValue());    // multexprprime.syn = arithexprprime1.syn
                    else
                        syn = multexprprime(inh.doubleValue() * simpleexprSyn.doubleValue());
                } else
                    throw new Exception("ARITHEXPRPRIME报错1");

            } else
                throw new Exception("SIMPLEEXPR报错1");
        } else if (temp.equals(TokenType.DIVIDE.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.SIMPLEEXPR.getValue())) {
                position++;
                Number simpleexprSyn = simpleexpr();
                if (simpleexprSyn == null)
                    throw new Exception("multexprprime解析出错2");
                if (productionList.get(position).equals(NonTerminalType.MULTEXPRPRIME.getValue())) {
                    position++;
//                    String left = PrintNewSymbol();
//                    if (!StoreQueue.peek().equals(left))
//                        System.out.println(left + "=" + inh.intValue() + "/" + StoreQueue.poll());
//                    else
//                        System.out.println(left + "=" + inh.intValue() + "/" + simpleexprSyn);
                    if (inh instanceof Integer && simpleexprSyn instanceof Integer)             // arithexprprime1.inh = multexprprime.inh / simpleexpr.syn
                        syn = multexprprime(inh.intValue() / simpleexprSyn.intValue());    // multexprprime.syn = arithexprprime1.syn
                    else
                        syn = multexprprime(inh.doubleValue() / simpleexprSyn.doubleValue());
                } else
                    throw new Exception("ARITHEXPRPRIME报错2");
            } else
                throw new Exception("SIMPLEEXPR报错2");
        } else if (productionList.get(position).equals(TokenType.EPSILON.getValue())) {
            position++;
        } else throw new Exception("multexprprime报错");
        return syn;
    }

    public Number arithexprprime(Number inh) throws Exception {
        String temp = productionList.get(position);
        Number syn = inh;
        if (temp.equals(TokenType.PLUS.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.MULTEXPR.getValue())) {
                position++;
                Number multexprSyn = multexpr();
                if (multexprSyn == null)
                    throw new Exception("arithexprprime解析错误1");
                if (productionList.get(position).equals(NonTerminalType.ARITHEXPRPRIME.getValue())) {
                    position++;
//                    String left = PrintNewSymbol();
//                    if (!StoreQueue.peek().equals(left))
//                        System.out.println(left + "=" + inh.intValue() + "+" + StoreQueue.poll());
//                    else
//                        System.out.println(left + "=" + inh.intValue() + "+" + multexprSyn);
                    if (inh instanceof Integer && multexprSyn instanceof Integer)             // arithexprprime1.inh = multexprprime.inh + simpleexpr.syn
                        syn = arithexprprime(inh.intValue() + multexprSyn.intValue());    // multexprprime.syn = arithexprprime1.syn
                    else
                        syn = arithexprprime(inh.doubleValue() + multexprSyn.doubleValue());
                } else
                    throw new Exception("SIMPLEEXPR报错3");

            } else
                throw new Exception("MULTEXPR报错1");

        } else if (temp.equals(TokenType.MINUS.getValue())) {
            position++;
            if (productionList.get(position).equals(NonTerminalType.MULTEXPR.getValue())) {
                position++;
                Number multexprSyn = multexpr();
                if (multexprSyn == null)
                    throw new Exception("arithexprprime解析错误2");
                if (productionList.get(position).equals(NonTerminalType.ARITHEXPRPRIME.getValue())) {
                    position++;
//                    String left = PrintNewSymbol();
//                    if (!StoreQueue.peek().equals(left))
//                        System.out.println(left + "=" + inh.intValue() + "-" + StoreQueue.poll());
//                    else
//                        System.out.println(left + "=" + inh.intValue() + "-" + multexprSyn);
                    if (inh instanceof Integer && multexprSyn instanceof Integer)             // arithexprprime1.inh = multexprprime.inh - simpleexpr.syn
                        syn = arithexprprime(inh.intValue() - multexprSyn.intValue());    // multexprprime.syn = arithexprprime1.syn
                    else
                        syn = arithexprprime(inh.doubleValue() - multexprSyn.doubleValue());
                } else
                    throw new Exception("SIMPLEEXPR报错4");
            } else
                throw new Exception("MULTEXPR报错2");
        } else if (productionList.get(position).equals(TokenType.EPSILON.getValue())) {
            position++;
        } else throw new Exception("arithexprprime报错");
        return syn;
    }

}
