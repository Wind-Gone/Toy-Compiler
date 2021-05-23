package com.example.compiler.llParser;

/**
 * 非终结符
 */
public enum NonTerminalType {
    PROGRAM,
    STMT,
    COMPOUNDSTMT,
    STMTS,
    IFSTMT,
    WHILESTMT,
    ASSGSTMT,
    BOOLEXPR,
    BOOLOP,
    ARITHEXPR,
    ARITHEXPRPRIME,
    MULTEXPR,
    MULTEXPRPRIME,
    SIMPLEEXPR,
}
