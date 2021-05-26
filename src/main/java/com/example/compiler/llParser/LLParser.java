package com.example.compiler.llParser;

import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Token;
import com.example.compiler.token.TokenType;

import java.util.*;

public class LLParser {
    private final Grammer grammer;
    private final ParsingTable parsingTable;
    private final HashMap<String, TreeSet<String>> firstSet;
    private final HashMap<String, TreeSet<String>> followSet;
    private List<TokenType> w;
    private Stack<Object> stk;
    private final List<Production> productions;
    private final HashSet<String> VnSet = new HashSet<>();//非终结符Vn集合
    private int id = 0;


    public LLParser(String input) {
        grammer = new Grammer();
        parsingTable = new ParsingTable();
        productions = new ArrayList<>();
        firstSet = new HashMap<>();
        followSet = new HashMap<>();
        stk_init();
        w_init(input);
        lmDerivation();
        VnSet.addAll(Collections.singletonList(Arrays.toString(NonTerminalType.values())));
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


    public void getFirstSet() {
        Arrays.asList(NonTerminalType.values())
                .forEach(item -> getFirst(item.toString()));
    }

    /*
    获取First集
     */
    public void getFirst(String item) {
        TreeSet<String> treeSet = firstSet.containsKey(item) ? firstSet.get(item) : new TreeSet<>();
        Set<Map.Entry<Integer, Production>> set = grammer.getProductions().entrySet();
        ArrayList<String> item_production = new ArrayList<>();
        for (Map.Entry<Integer, Production> integerProductionEntry : set) {
            Production production = integerProductionEntry.getValue();
            NonTerminalType left_expr = production.getLeftExpression();
            List<Object> right_expr = production.getRightExpression();
            if (left_expr.getValue().equals(item)) {
                item_production.add(right_expr.get(0).toString());
            }
        }
        System.out.println("********");
        for (String s : item_production)
            System.out.println("hu" + s);
        System.out.println("#######");
//        if (!VnSet.contains(item)){
//            treeSet.add(item);
//            firstSet.put(item,treeSet);
//            return;
//        }
//        else {
//        }
    }
//    /*
//    获取Follow集
//     */
//    // TODO
//    public void getFollowSet() {
//
//    }

}
