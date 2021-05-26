package com.example.compiler.token;

public enum TokenType {
    /**
     * 注释
     */
    COMMENTS("COMMENTS"),

    /**
     * 特殊数字先判断
     */
    REALNUMBER("REALNUMBER"),
    EXPONENT("EXPONENT"),
    FRACTION("FRACTION"),




    /**
     * keywords
     */
    IF("IF"),
    THEN("THEN"),
    ELSE("ELSE"),
    WHILE("WHILE"),

    /**
     * 标识符
     */
    IDENTIFIERS("IDENTIFIERS"),

    /**
     * operator
     */
    PLUS("PLUS"),
    MINUS("MINUS"),
    DIVIDE("DIVIDE"),
    MULTIPLY("MULTIPLY"),
    EQUAL("EQUAL"),
    EQUALEQUAL("EQUALEQUAL"),
    LESS("LESS"),
    LESSEQUAL("LESSEQUAL"),
    GREATER("GREATER"),
    GREATEREQUAL("GREATEREQUAL"),
    NOTEQUAL("NOTEQUAL"),

    /**
     * delimiters
     */
    OPENBRACE("OPENBRACE"),       // (
    CLOSEBRACE("CLOSEBRACE"),      // )
    OPENCURLYBRACE("OPENCURLYBRACE"),  // {
    CLOSECURLYBRACE("CLOSECURLYBRACE"), // }
    SEMICOLON("SEMICOLON"),       // ;
    COMMA("COMMA"),           // ,

    /**
     * numbers
     */
    DIGIT("DIGIT"),
    INTNUMBER("INTNUMBER"),



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
    NUM("NUM"),

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
        return  this == NEWLINE || this == TAB || this == WHITESPACE || this == ENTER;
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
