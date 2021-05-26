package com.example.compiler.llParser;

import com.example.compiler.token.Token;
import com.example.compiler.token.TokenType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Grammer ： 即所有产生式
 */
public class Grammer {
    /**
     * productions ： (序号 + 产生式) (序号 + 产生式) ...
     */
    private final LinkedHashMap<Integer, Production> productions;

    public LinkedHashMap<Integer, Production> getProductions() {
        return productions;
    }

    /**
     * @param label
     * @return label对应的产生式
     */
    public Production get(int label) {
        return productions.get(label);
    }

    public Grammer() {
        productions = new LinkedHashMap<>();

        productions.put(1, new Production(NonTerminalType.PROGRAM, new ArrayList<Object>() {{
            add(NonTerminalType.COMPOUNDSTMT);
        }}));
        productions.put(2, new Production(NonTerminalType.STMT, new ArrayList<Object>() {{
            add(NonTerminalType.IFSTMT);
        }}));
        productions.put(3, new Production(NonTerminalType.STMT, new ArrayList<Object>() {{
            add(NonTerminalType.WHILESTMT);
        }}));
        productions.put(4, new Production(NonTerminalType.STMT, new ArrayList<Object>() {{
            add(NonTerminalType.ASSGSTMT);
        }}));
        productions.put(5, new Production(NonTerminalType.STMT, new ArrayList<Object>() {{
            add(NonTerminalType.COMPOUNDSTMT);
        }}));
        productions.put(6, new Production(NonTerminalType.COMPOUNDSTMT, new ArrayList<Object>() {{
            add(TokenType.OPENCURLYBRACE);
            add(NonTerminalType.STMTS);
            add(TokenType.CLOSECURLYBRACE);
        }}));
        productions.put(7, new Production(NonTerminalType.STMTS, new ArrayList<Object>() {{
            add(NonTerminalType.STMT);
            add(NonTerminalType.STMTS);
        }}));
        productions.put(8, new Production(NonTerminalType.STMTS, new ArrayList<Object>() {{
            add(TokenType.EPSILON);
        }}));
        productions.put(9, new Production(NonTerminalType.IFSTMT, new ArrayList<Object>() {{
            add(TokenType.IF);
            add(TokenType.OPENBRACE);
            add(NonTerminalType.BOOLEXPR);
            add(TokenType.CLOSEBRACE);
            add(TokenType.THEN);
            add(NonTerminalType.STMT);
            add(TokenType.ELSE);
            add(NonTerminalType.STMT);
        }}));
        productions.put(10, new Production(NonTerminalType.WHILESTMT, new ArrayList<Object>() {{
            add(TokenType.WHILE);
            add(TokenType.OPENBRACE);
            add(NonTerminalType.BOOLEXPR);
            add(TokenType.CLOSEBRACE);
            add(NonTerminalType.STMT);
        }}));
        productions.put(11, new Production(NonTerminalType.ASSGSTMT, new ArrayList<Object>() {{
            add(TokenType.IDENTIFIERS);
            add(TokenType.EQUAL);
            add(NonTerminalType.ARITHEXPR);
            add(TokenType.SEMICOLON);
        }}));
        productions.put(12, new Production(NonTerminalType.BOOLEXPR, new ArrayList<Object>() {{
            add(NonTerminalType.ARITHEXPR);
            add(NonTerminalType.BOOLOP);
            add(NonTerminalType.ARITHEXPR);
        }}));
        productions.put(13, new Production(NonTerminalType.BOOLOP, new ArrayList<Object>() {{
            add(TokenType.LESS);
        }}));
        productions.put(14, new Production(NonTerminalType.BOOLOP, new ArrayList<Object>() {{
            add(TokenType.GREATER);
        }}));
        productions.put(15, new Production(NonTerminalType.BOOLOP, new ArrayList<Object>() {{
            add(TokenType.LESSEQUAL);
        }}));
        productions.put(16, new Production(NonTerminalType.BOOLOP, new ArrayList<Object>() {{
            add(TokenType.GREATEREQUAL);
        }}));
        productions.put(17, new Production(NonTerminalType.BOOLOP, new ArrayList<Object>() {{
            add(TokenType.EQUALEQUAL);
        }}));
        productions.put(18, new Production(NonTerminalType.ARITHEXPR, new ArrayList<Object>() {{
            add(NonTerminalType.MULTEXPR);
            add(NonTerminalType.MULTEXPRPRIME);
        }}));
        productions.put(19, new Production(NonTerminalType.ARITHEXPRPRIME, new ArrayList<Object>() {{
            add(TokenType.PLUS);
            add(NonTerminalType.MULTEXPR);
            add(NonTerminalType.ARITHEXPRPRIME);
        }}));
        productions.put(20, new Production(NonTerminalType.ARITHEXPRPRIME, new ArrayList<Object>() {{
            add(TokenType.MINUS);
            add(NonTerminalType.MULTEXPR);
            add(NonTerminalType.ARITHEXPRPRIME);
        }}));
        productions.put(21, new Production(NonTerminalType.ARITHEXPRPRIME, new ArrayList<Object>() {{
            add(TokenType.EPSILON);
        }}));
        productions.put(22, new Production(NonTerminalType.MULTEXPR, new ArrayList<Object>() {{
            add(NonTerminalType.SIMPLEEXPR);
            add(NonTerminalType.MULTEXPRPRIME);
        }}));
        productions.put(23, new Production(NonTerminalType.MULTEXPRPRIME, new ArrayList<Object>() {{
            add(TokenType.MULTIPLY);
            add(NonTerminalType.SIMPLEEXPR);
            add(NonTerminalType.MULTEXPRPRIME);
        }}));
        productions.put(24, new Production(NonTerminalType.MULTEXPRPRIME, new ArrayList<Object>() {{
            add(TokenType.DIVIDE);
            add(NonTerminalType.SIMPLEEXPR);
            add(NonTerminalType.MULTEXPRPRIME);
        }}));
        productions.put(25, new Production(NonTerminalType.MULTEXPRPRIME, new ArrayList<Object>() {{
            add(TokenType.EPSILON);
        }}));
        productions.put(26, new Production(NonTerminalType.SIMPLEEXPR, new ArrayList<Object>() {{
            add(TokenType.IDENTIFIERS);
        }}));
        productions.put(27, new Production(NonTerminalType.SIMPLEEXPR, new ArrayList<Object>() {{
            add(TokenType.NUM);
        }}));
        productions.put(28, new Production(NonTerminalType.SIMPLEEXPR, new ArrayList<Object>() {{
            add(TokenType.EPSILON);
        }}));


    }
}
