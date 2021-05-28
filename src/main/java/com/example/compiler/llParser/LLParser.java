package com.example.compiler.llParser;

import com.example.compiler.entity.ErrorCode;
import com.example.compiler.entity.WrongMessage;
import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Token;
import com.example.compiler.token.TokenType;
import javafx.util.Pair;

import java.util.*;

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
    private String[][] finalTable;
    private static String start = "PROGRAM";
    private final HashMap<String, ArrayList<List<Object>>> productionMap = new HashMap<>();
    private List<Token> w;              // 分析程序的输入串
    private Stack<Object> stk;          // 分析程序的栈
    private int id = 0;


    public LLParser(String input) {
        grammer = new Grammer();
        parsingTable = new ParsingTable();
        productions = new ArrayList<>();
        firstSet = new HashMap<>();
        firstSet2 = new HashMap<>();
        followSet = new HashMap<>();
        stk_init();
        w_init(input);
//        lmDerivation();
        init();
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
//        System.out.println("------w-------------:  "+ w );
        int ip = 0;
        Object X = stk.peek();
        while (X != TokenType.DOLLAR) {
            System.out.println("------stk-------------: " + stk);
            System.out.println("------token-------------: " + w.get(ip).getTokenType());
//            System.out.println("------当前X-----: "+X);
            TokenType a = w.get(ip).getTokenType();
            if (X == a) {
//                System.out.println("-----跳入1---------a 为：" + a);
                stk.pop();
                ip++;
            } else if (X instanceof TokenType) {        // X是终结符
//                System.out.println("-----跳入2---------a 为：" + a);
                error(X, w.get(ip));
                break;
            } else if (parsingTable.get((NonTerminalType) X, a) == -1) {
//                System.out.println("-----跳入3--------- a 为：" + a);
//                System.out.println("-----跳入3--------- X 为：" + X);
                error(X, w.get(ip));
                break;
            } else {
//                System.out.println("-----跳入4---------a 为：" + a);
//                System.out.print("当前栈：" + X + "  action:  ");
                Production production = grammer.get(parsingTable.get((NonTerminalType) X, a));
//                System.out.println(production);
                production.setId(id);
                productions.add(production);
                id++;
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
        printParseTree(productions);

    }

    /**
     * 错误恢复
     */
    private void error(Object X, Token a) {
        System.out.println("--------error--------" + X + "  " + a);
        if (X instanceof TokenType) {        // 如果是个终结符，就直接弹栈尝试继续分析 ? 怎么下面例子是跳过了输入
            stk.pop();
        } else if (X instanceof NonTerminalType) {
            TreeSet<String> firstSetForX = firstSet.get(X.toString());
            TreeSet<String> followSetForX = followSet.get(X.toString());
            LLUtil llUtil = new LLUtil();
            HashMap<Pair<NonTerminalType, TokenType>, Object> parsingTable = llUtil.getParsingTable();

        }
        WrongMessage wrongMessage = new WrongMessage(X.toString(), ErrorCode.NOT_MATCH_FOR_GRAMMER);
        wrongList.put(new Pair<>(((Token) X).getRow(), ((Token) X).getColumn()), wrongMessage);
    }

    /**
     * 打印语法树
     */
    private void printParseTree(List<Production> productions) {
        Production p = productions.get(0);
        recurseProduction(p);
    }

    /**
     * 递归调用打印预测分析步骤
     */
    public void recurseProduction(Production p) {
        List<Object> rightExpression = p.getRightExpression();
        ListIterator<Object> iterator = rightExpression.listIterator();
        while (iterator.hasNext()) {
            Object s = iterator.next();
            if (s instanceof TokenType) {
                System.out.println(s);
                if (!iterator.hasNext())
                    return;
            } else if (s instanceof NonTerminalType) {
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
    }

    /*
    计算所有的First集
     */
    public void getFirstSet() {
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> getFirst(item.toString()));
    }

    /*
    计算单个字符串的First集
     */
    public void getFirst(String item) {
        TreeSet<String> treeSet = firstSet.containsKey(item) ? firstSet.get(item) : new TreeSet<>();    //如果已经存在了这个key就直接获取否则新建一个
        ArrayList<List<Object>> item_production = productionMap.get(item.toString());
        if (!VnSet.contains(item)) {                // 如果是终结符
            treeSet.add(item);
            firstSet.put(item, treeSet);
            return;
        } else {
            for (List<Object> s : item_production) {
                int i = 0;
                while (i < s.size()) {
                    String str = s.get(i).toString();
                    getFirst(str);
                    TreeSet<String> tvSet = firstSet.get(str);
                    for (String tmp : tvSet) {
                        if (tmp != "EPSILON")
                            treeSet.add(tmp);
                    }
                    if (tvSet.contains("EPSILON"))
                        i++;
                    else
                        break;
                }
                if (i == s.size())
                    treeSet.add("EPSILON");
            }
            firstSet.put(item, treeSet);
        }
    }

    /*
    计算某个字符串列表的First集
     */
    public void getFirstList(List<Object> s) {
        TreeSet<String> set = (firstSet2.containsKey(s)) ? firstSet2.get(s) : new TreeSet<>();
        int i = 0;
        while (i < s.size()) {
            String tn = s.get(i).toString();
            if (!firstSet.containsKey(tn))
                getFirst(tn);
            TreeSet<String> tvSet = firstSet.get(tn);
            for (String tmp : tvSet)
                if (tmp != "EPSILON")
                    set.add(tmp);
            if (tvSet.contains("EPSILON"))
                i++;
            else
                break;
            if (i == s.size()) {
                set.add("EPSILON");
            }
        }
//        System.out.println("  " + s + "  " + set);
        firstSet2.put(s, set);
    }

    /*
    计算所有的Follow集
     */
    public void getFollowSet() {
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> getFollow(item.toString()));
    }

    /**
     * 生成所有的非终结符的产生式
     */
    public void init() {
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> VnSet.add(item.getValue()));
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> createProduces(item.getValue()));
        String Terminal = "OPENCURLYBRACE,CLOSECURLYBRACE,IF,OPENBRACE,CLOSEBRACE,THEN,ELSE,WHILE,IDENTIFIERS,EQUAL,SEMICOLON,LESS,GREATER,LESSEQUAL,GREATEREQUAL,EQUALEQUAL,PLUS,MINUS,MULTIPLY,DIVIDE,NUM,DOLLAR";
        VtSet = new HashSet<>(Arrays.asList(Terminal.split(",")));
        getFirstSet();
        getFollowSet();
    }

    /**
     * 具体的执行函数
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
     * 具体的执行函数
     */
    public void printproductionMap() {
        for (Map.Entry<String, ArrayList<List<Object>>> entry : productionMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    /**
     * 得到单个字符串的follow集
     */
    public void getFollow(String ch) {
        TreeSet<String> set = followSet.containsKey(ch) ? followSet.get(ch) : new TreeSet<>();
        if (ch == start) {
            set.add(TokenType.DOLLAR.toString());
            followSet.put(ch, set);
        }
        for (Map.Entry<String, ArrayList<List<Object>>> entry : productionMap.entrySet()) {
            String key = entry.getKey().toString();
            ArrayList<List<Object>> item_production = entry.getValue();
            for (List<Object> s : item_production) {
                for (int i = 0; i < s.size(); i++) {
                    if (s.get(i).toString() == ch) {
                        if (i == (s.size() - 1)) {
                            TreeSet<String> tempSet = null;
                            if (key != ch) {
                                if (followSet.containsKey(key))
                                    tempSet = followSet.get(key);
                                else {
                                    getFollow(key);
                                    tempSet = followSet.get(key);
                                }
                                set.addAll(tempSet);
                                followSet.put(ch, set);
                            }

                        } else {
                            int j = i + 1;
                            List<Object> templist = s.subList(j, s.size());
                            if (!firstSet2.containsKey(templist))
                                getFirstList(templist);
                            TreeSet<String> tempSet = firstSet2.get(templist);
//                            System.out.println("this" + ch);
//                            for (String str : tempSet) {
//                                System.out.println(str);
//                            }
                            if (tempSet.contains("EPSILON")) {
                                tempSet.remove("EPSILON");
                                set.addAll(tempSet);
                                TreeSet<String> tempSet3 = null;
                                if (ch != key) {
                                    if (followSet.containsKey(key))
                                        tempSet3 = followSet.get(key);
                                    else {
                                        getFollow(key);
                                        tempSet3 = followSet.get(key);
                                    }
                                    set.addAll(tempSet3);
                                }
                            } else {
                                set.addAll(tempSet);
                            }
                            followSet.put(ch, set);
                        }
                    }
                }
            }
        }
    }

//    /**
//     * 通过first/follow构建表
//     */
//    public void buildTable() {
//        Object[] VnArray = VnSet.toArray();
//        Object[] VtArray = VtSet.toArray();
//        finalTable = new String[VnArray.length + 1][VtArray.length + 1];
//        finalTable[0][0] = "NT\\T";
//        for (int i = 0; i < VtArray.length; i++)
//            finalTable[0][i + 1] = VtArray[i].toString();
//        for (int i = 0; i < VnArray.length; i++)
//            finalTable[i + 1][0] = VnArray[i] + "";
//
//    }

//    /**
//     * 将生成式插入表中
//     */
//    private void insertTable(String a, String ch, List<Object> l) {
//        if (ch == TokenType.EPSILON.getValue()){
//            System.out.println("This is EPSILON");
//            ch = TokenType.DOLLAR.getValue();
//        }
//        for (int i = 0; i < VnSet.size() + 1; i++) {
//
//        }
//    }

    // 打印first集和follow集
    public void printFirstAFollow() {
        for (Map.Entry<String, TreeSet<String>> entry : firstSet.entrySet()) {/// first(A)
            String key = entry.getKey();
            System.out.print("FIRST(" + key + ")= ");
            TreeSet<String> set = firstSet.get(key);
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " ");
            }
            System.out.println();
        }
        for (Map.Entry<String, TreeSet<String>> entry : followSet.entrySet()) {
            String key = entry.getKey();
            System.out.print("FOLLOW(" + key + ")= ");
            TreeSet<String> set = followSet.get(key);
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " ");
            }
            System.out.println();
        }
    }

}
