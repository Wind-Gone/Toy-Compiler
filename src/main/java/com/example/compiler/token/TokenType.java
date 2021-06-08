package com.example.compiler.token;

public enum TokenType {
    /**
     * 注释
     */
    COMMENTS("comments"),

    /**
     * 特殊数字先判断
     */
    REALNUMBER("realnumber"),
    EXPONENT("exponent"),
    FRACTION("fraction"),



    /**
     * keywords
     */
    IF("if"),
    THEN("then"),
    ELSE("else"),
    WHILE("while"),

    /**
     * 标识符
     */
    IDENTIFIERS("identifiers"),

    /**
     * operator
     */
    PLUS("+"),
    MINUS("-"),
    DIVIDE("/"),
    MULTIPLY("*"),
    EQUALEQUAL("=="),
    LESSEQUAL("<="),
    GREATEREQUAL(">="),
    EQUAL("="),
    LESS("<"),
    GREATER(">"),


    /**
     * delimiters
     */
    OPENBRACE("("),       // (
    CLOSEBRACE(")"),      // )
    OPENCURLYBRACE("{"),  // {
    CLOSECURLYBRACE("}"), // }
    SEMICOLON(";"),       // ;
    COMMA(","),           // ,

    /**
     * numbers
     */
    DIGIT("digit"),
    INTNUMBER("intnumber"),


    /**
     * 空格 TAB 回车
     */
    WHITESPACE("WHITESPACE"),
    NEWLINE("NEWLINE"),
    TAB("TAB"),
    ENTER("ENTER"),

    /**
     * 数字 DIGIT + INT + E + ...
     */
    NUM("num"),

    /**
     * 美元符
     */
    DOLLAR("DOLLAR"),

    /**
     * epsilon
     */
    EPSILON("EPSILON");


    /**
     * Determines if this token is auxiliary
     *
     * @return {@code true} if token is auxiliary, {@code false} otherwise
     */
    public boolean isAuxiliary() {
        return this == NEWLINE || this == TAB || this == WHITESPACE || this == ENTER || this == COMMENTS;
    }

    private final String value;

    TokenType(final String value) {
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
