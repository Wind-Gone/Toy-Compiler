package com.example.compiler.llParser;

import com.example.compiler.entity.token.TokenType;
import javafx.util.Pair;

import java.util.LinkedHashMap;

public class ParsingTable {
    private final LinkedHashMap<Pair<NonTerminalType, TokenType>, Integer> parsingTable;

    /**
     * @param nonTerminalType
     * @param tokenType
     * @return 返回label ，若不存在返回-1;
     */
    public int get(NonTerminalType nonTerminalType, TokenType tokenType) {
        if (parsingTable.get(new Pair<>(nonTerminalType, tokenType)) == null) {
            return -1;
        }
        return parsingTable.get(new Pair<>(nonTerminalType, tokenType));
    }

    public ParsingTable() {
        parsingTable = new LinkedHashMap<>();
        parsingTable.put(new Pair<>(NonTerminalType.PROGRAM, TokenType.OPENCURLYBRACE), 1);
        parsingTable.put(new Pair<>(NonTerminalType.STMT, TokenType.OPENCURLYBRACE), 5);
        parsingTable.put(new Pair<>(NonTerminalType.STMT, TokenType.IF), 2);
        parsingTable.put(new Pair<>(NonTerminalType.STMT, TokenType.WHILE), 3);
        parsingTable.put(new Pair<>(NonTerminalType.STMT, TokenType.IDENTIFIERS), 4);
        parsingTable.put(new Pair<>(NonTerminalType.COMPOUNDSTMT, TokenType.OPENCURLYBRACE), 6);
        parsingTable.put(new Pair<>(NonTerminalType.STMTS, TokenType.OPENCURLYBRACE), 7);
        parsingTable.put(new Pair<>(NonTerminalType.STMTS, TokenType.CLOSECURLYBRACE), 8);
        parsingTable.put(new Pair<>(NonTerminalType.STMTS, TokenType.IF), 7);
        parsingTable.put(new Pair<>(NonTerminalType.STMTS, TokenType.WHILE), 7);
        parsingTable.put(new Pair<>(NonTerminalType.STMTS, TokenType.IDENTIFIERS), 7);
        parsingTable.put(new Pair<>(NonTerminalType.IFSTMT, TokenType.IF), 9);
        parsingTable.put(new Pair<>(NonTerminalType.WHILESTMT, TokenType.WHILE), 10);
        parsingTable.put(new Pair<>(NonTerminalType.ASSGSTMT, TokenType.IDENTIFIERS), 11);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLEXPR, TokenType.OPENBRACE), 12);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLEXPR, TokenType.IDENTIFIERS), 12);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLEXPR, TokenType.NUM), 12);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLOP, TokenType.LESS), 13);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLOP, TokenType.GREATER), 14);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLOP, TokenType.LESSEQUAL), 15);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLOP, TokenType.GREATEREQUAL), 16);
        parsingTable.put(new Pair<>(NonTerminalType.BOOLOP, TokenType.EQUALEQUAL), 17);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPR, TokenType.OPENBRACE), 18);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPR, TokenType.IDENTIFIERS), 18);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPR, TokenType.NUM), 18);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.CLOSEBRACE), 21);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.SEMICOLON), 21);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.LESS), 21);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.GREATER), 21);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.LESSEQUAL), 21);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.GREATEREQUAL), 21);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.EQUALEQUAL), 21);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.PLUS), 19);
        parsingTable.put(new Pair<>(NonTerminalType.ARITHEXPRPRIME, TokenType.MINUS), 20);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPR, TokenType.OPENBRACE), 22);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPR, TokenType.IDENTIFIERS), 22);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPR, TokenType.NUM), 22);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.CLOSEBRACE), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.SEMICOLON), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.LESS), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.GREATER), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.LESSEQUAL), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.GREATEREQUAL), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.EQUALEQUAL), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.PLUS), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.MINUS), 25);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.MULTIPLY), 23);
        parsingTable.put(new Pair<>(NonTerminalType.MULTEXPRPRIME, TokenType.DIVIDE), 24);
        parsingTable.put(new Pair<>(NonTerminalType.SIMPLEEXPR, TokenType.OPENBRACE), 28);
        parsingTable.put(new Pair<>(NonTerminalType.SIMPLEEXPR, TokenType.IDENTIFIERS), 26);
        parsingTable.put(new Pair<>(NonTerminalType.SIMPLEEXPR, TokenType.NUM), 27);
    }

}
