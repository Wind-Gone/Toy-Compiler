package com.example.compiler.llParser;

import com.example.compiler.token.TokenType;

import java.util.List;

/**
 * Production : 即产生式 ,如： compoundstmt ->  { stmts }
 */
public class Production {
    private final NonTerminalType leftExpression;
    private final List<Object> rightExpression;

    /**
     * @param leftExpression  产生式左侧为一个非终结符
     * @param rightExpression 产生式右侧为终结符和非终结符的list
     */
    public Production(NonTerminalType leftExpression, List<Object> rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    public NonTerminalType getLeftExpression() {
        return leftExpression;
    }

    public List<Object> getRightExpression() {
        return rightExpression;
    }


    @Override
    public String toString() {
        return " " + leftExpression + " -> " + rightExpression;
    }

}
