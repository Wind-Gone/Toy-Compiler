package com.example.compiler.llParser;

/**
 * 非终结符
 */
public enum NonTerminalType {
    PROGRAM("PROGRAM"),
    STMT("STMT"),
    COMPOUNDSTMT("COMPOUNDSTMT"),
    STMTS("STMTS"),
    IFSTMT("IFSTMT"),
    WHILESTMT("WHILESTMT"),
    ASSGSTMT("ASSGSTMT"),
    BOOLEXPR("BOOLEXPR"),
    BOOLOP("BOOLOP"),
    ARITHEXPR("ARITHEXPR"),
    ARITHEXPRPRIME("ARITHEXPRPRIME"),
    MULTEXPR("MULTEXPR"),
    MULTEXPRPRIME("MULTEXPRPRIME"),
    SIMPLEEXPR("SIMPLEEXPR");
    private final String value;

    NonTerminalType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
