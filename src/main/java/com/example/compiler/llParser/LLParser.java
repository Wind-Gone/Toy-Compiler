package com.example.compiler.llParser;

import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Token;
import com.example.compiler.token.TokenType;

import java.util.*;

@SuppressWarnings("all")
public class LLParser {
    private final Grammer grammer;
    private final ParsingTable parsingTable;
    private final HashMap<String, TreeSet<String>> firstSet;
    private final HashMap<String, TreeSet<String>> followSet;
    private final HashMap<List<Object>, TreeSet<String>> firstSet2;
    private List<TokenType> w;
    private Stack<Object> stk;
    private final List<Production> productions;
    private final HashSet<String> VnSet = new HashSet<>();//非终结符Vn集合
    private static String start = "PROGRAM";
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
        lmDerivation();
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> VnSet.add(item.getValue()));
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
                type = TokenType.NUM;
            }
            w.add(type);
        }
        w.add(TokenType.DOLLAR);
    }

    /**
     * 输出最左推导 -- 书上的伪代码
     */
    private void lmDerivation() {
        int ip = 0;
        Object X = stk.peek();
        while (X != TokenType.DOLLAR) {
//            System.out.println("------当前X-----: "+X);
            TokenType a = w.get(ip);
            if (X == a) {
//                System.out.println("-----跳入1---------a 为：" + a);
                stk.pop();
                ip++;
            } else if (X instanceof TokenType) {
//                System.out.println("-----跳入2---------a 为：" + a);
                error(X);
                break;
            } else if (parsingTable.get((NonTerminalType) X, a) == -1) {
//                System.out.println("-----跳入3--------- a 为：" + a);
                error(X);
                break;
            } else {
//                System.out.println("-----跳入4---------a 为：" + a);
                System.out.print("当前栈：" + X + "  action:  ");
                Production production = grammer.get(parsingTable.get((NonTerminalType) X, a));
                System.out.println(production);
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

        printParsingTree(productions);

    }

    /**
     * 书上就一个函数名，我也不知道咋写
     */
    private void error(Object X) {
        System.out.println(X);
    }

    private void printParsingTree(List<Production> productions) {
        Production p = productions.get(0);
        recurseProduction(p);
    }


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
        获取First集
         */
    public void getFirstSet() {
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> getFirst(item.toString()));
        for (Map.Entry<String, TreeSet<String>> entry : firstSet.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }


    public void getFirst(String item) {
        TreeSet<String> treeSet = firstSet.containsKey(item) ? firstSet.get(item) : new TreeSet<>();    //如果已经存在了这个key就直接获取否则新建一个
        Set<Map.Entry<Integer, Production>> set = grammer.getProductions().entrySet();
        ArrayList<List<Object>> item_production = new ArrayList<>();
        for (Map.Entry<Integer, Production> integerProductionEntry : set) {
            Production production = integerProductionEntry.getValue();
            NonTerminalType left_expr = production.getLeftExpression();
            List<Object> right_expr = production.getRightExpression();
            if (left_expr.getValue().equals(item)) {
                item_production.add(right_expr);
            }
        }
//        for (List<Object> list : item_production) {
//            System.out.println(item);
//            System.out.println(list.toString());
////            System.out.println("&*******");
////            System.out.println(list.get(0));
////            System.out.println("######");
//        }
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

    public void getFirstList(List<Object> list) {
        TreeSet<String> treeSet = firstSet2.containsKey(list) ? firstSet2.get(list) : new TreeSet<>();    //如果已经存在了这个key就直接获取否则新建一个
        int index = 0;
        while (index < list.size()) {
            String str = list.get(index).toString();
            if (!firstSet.containsKey(str))
                getFirst(str);
            TreeSet<String> tmpSet = firstSet.get(str);
            for (String tmp : tmpSet) {
                if (tmp != "EPSILON")
                    treeSet.add(tmp);
            }
            if (tmpSet.contains("EPSILON"))
                index++;
            else
                break;
            if (index == list.size())
                treeSet.add("EPSILON");
        }
        firstSet2.put(list, treeSet);
    }

    public void getFollowSet() {
        for (int i = 0; i < 3; i++) {
            Arrays.asList(NonTerminalType.values())
                    .forEach(item -> getFollow(item.toString()));
        }
        for (Map.Entry<String, TreeSet<String>> entry : followSet.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    public void getFollow(String item) {
        TreeSet<String> setA = followSet.containsKey(item) ? followSet.get(item) : new TreeSet<>();
        Set<Map.Entry<Integer, Production>> set = grammer.getProductions().entrySet();
        ArrayList<List<Object>> item_production = new ArrayList<>();
        for (Map.Entry<Integer, Production> integerProductionEntry : set) {     //得到所有产生式
            Production production = integerProductionEntry.getValue();
            NonTerminalType left_expr = production.getLeftExpression();
            List<Object> right_expr = production.getRightExpression();
            if (left_expr.getValue().equals(item)) {
                item_production.add(right_expr);
            }
        }
        if (item == start) {
            setA.add(TokenType.DOLLAR.toString());
            followSet.put(item, setA);
        }

        for (List<Object> list : item_production) {
            int endIndex = list.size() - 1;
            while (endIndex >= 0) {
                String str = list.get(endIndex).toString();
                if (VnSet.contains(str)) {
                    // 假定都满足A->αBβ产生式
                    if (list.size() - endIndex <= 1) { //非终结符后没有字符，运用规则3
                        if (str != item) {
                            TreeSet<String> setB = followSet.containsKey(str) ? followSet.get(str) : new TreeSet<>();
                            setB.addAll(setA);
                            followSet.put(str, setB);
                        }
                    } else {                //非终结符后有字符，运用规则2
                        List<Object> templist = list.subList(endIndex + 1, list.size());
                        TreeSet<String> tempSet = null;
                        if (templist.size() == 1) {         // 如果β只有单个字符串，获取单个字符串的first集合
                            if (!firstSet.containsKey(templist.get(0)))
                                getFirst(templist.get(0).toString());
                            tempSet = firstSet.get(templist.get(0));
                        } else {                        // 如果β具有多个字符串，获取以列表形式的字符串的first集合
                            if (!firstSet2.containsKey(templist))
                                getFirstList(templist);
                            tempSet = firstSet2.get(templist);
                        }
                        TreeSet<String> setX = followSet.containsKey(str) ? followSet.get(str) : new TreeSet<>();   //更新str的follow集合
                        for (String var : tempSet)
                            if (var != "EPSILON")
                                setX.add(var);
                        followSet.put(str, setX);

                        // 若first(β)包含空串   followA 加入 followB
                        if (tempSet.contains("EPSILON")) {
                            if (item != str) {
                                TreeSet<String> setB = followSet.containsKey(str) ? followSet.get(str) : new TreeSet<>();
                                setB.addAll(setA);
                                followSet.put(str, setB);
                            }
                        }
                    }
                    endIndex--;
                } else
                    endIndex--;
            }
        }

    }

    public void buildTable() {

    }


}
