package com.example.compiler.llParser;

/**
 * 非终结符
 */
public enum NonTerminalType {
    PROGRAM("program"),
    STMT("stmt"),
    COMPOUNDSTMT("compoundstmt"),
    STMTS("stmts"),
    IFSTMT("ifstmt"),
    WHILESTMT("whilestmt"),
    ASSGSTMT("assgstmt"),
    BOOLEXPR("boolexpr"),
    BOOLOP("boolop"),
    ARITHEXPR("arithexpr"),
    ARITHEXPRPRIME("arithexprprime"),
    MULTEXPR("multexpr"),
    MULTEXPRPRIME("multexprptime"),
    SIMPLEEXPR("simpleexpr");
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
