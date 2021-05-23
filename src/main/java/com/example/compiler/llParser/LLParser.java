package com.example.compiler.llParser;

import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Token;
import com.example.compiler.token.TokenType;

import java.util.*;

public class LLParser {
    private final Grammer grammer;
    private final ParsingTable parsingTable;
    private HashMap<NonTerminalType, TreeSet<String>> firstSet;
    private HashMap<NonTerminalType, TreeSet<String>> followSet;
    private List<TokenType> w;
    private Stack<Object> stk;
    private List<Production> productions;
    private int id = 0;


    public LLParser(String input) {
        grammer = new Grammer();
        parsingTable = new ParsingTable();
        productions = new ArrayList<>();
        stk_init();
        w_init(input);
        lmDerivation();
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

    public void getFirstSet() {
        Set<Map.Entry<Integer, Production>> set = grammer.getProductions().entrySet();
        for (Map.Entry<Integer, Production> integerProductionEntry : set) {
            int label = integerProductionEntry.getKey();
            getFirst(label);
        }
    }

    /*
    获取First集
     */
    public void getFirst(int label) {
        NonTerminalType leftExpr = grammer.getProductions().get(label).getLeftExpression();
        List<Object> rightExprList = grammer.getProductions().get(label).getRightExpression();
        TreeSet<String> treeSet = firstSet.containsKey(leftExpr) ? firstSet.get(leftExpr) : new TreeSet<>();
        Object rightExpr = rightExprList.get(0);
        int i = 0;
        if (!(rightExpr instanceof NonTerminalType)) {
            String rightExpr2String = (String) rightExpr;
            treeSet.add(rightExpr2String);
            return;
        } else {
            while (i < rightExprList.size()) {
                for (Map.Entry<Integer, Production> entry : grammer.getProductions().entrySet()) {
                    if (entry.getValue().getLeftExpression().equals(rightExprList.get((i)))) {
                        getFirst(entry.getKey());
                    }
                }
                TreeSet<String> nonTerTreeSet = firstSet.get((NonTerminalType) rightExprList.get(i));
                for (String tmp : nonTerTreeSet) {
                    if (!tmp.equals("EPSILON"))
                        treeSet.add(tmp);
                }
                if (nonTerTreeSet.contains("EPSILON"))
                    i++;
                else
                    break;
            }
        }
        firstSet.put(leftExpr, treeSet);
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
    获取Follow集
     */
    // TODO
    public void getFollowSet() {

    }
}
