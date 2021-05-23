package com.example.compiler.token;

public enum TokenType {
    /**
     * 注释
     */
    COMMENTS,

    /**
     * 特殊数字先判断
     */
    REALNUMBER,
    EXPONENT,
    FRACTION,




    /**
     * keywords
     */
    IF,
    THEN,
    ELSE,
    WHILE,

    /**
     * 标识符
     */
    IDENTIFIERS,

    /**
     * operator
     */
    PLUS,
    MINUS,
    DIVIDE,
    MULTIPLY,
    EQUAL,
    EQUALEQUAL,
    LESS,
    LESSEQUAL,
    GREATER,
    GREATEREQUAL,
    NOTEQUAL,

    /**
     * delimiters
     */
    OPENBRACE,       // (
    CLOSEBRACE,      // )
    OPENCURLYBRACE,  // {
    CLOSECURLYBRACE, // }
    SEMICOLON,       // ;
    COMMA,           // ,

    /**
     * numbers
     */
    DIGIT,
    INTNUMBER,



    /**
     * 空格 TAB 回车
     */
    WHITESPACE,
    NEWLINE,
    TAB,
    ENTER,

    /**
     * 数字 DIGIT + INT + E + ...
     */
    NUM,

    /**
     * 美元符
     */
    DOLLAR,

    /**
     * epsilon
     */
    EPSILON;


    /**
     * Determines if this token is auxiliary
     *
     * @return {@code true} if token is auxiliary, {@code false} otherwise
     */
    public boolean isAuxiliary() {
        return  this == NEWLINE || this == TAB || this == WHITESPACE || this == ENTER;
    }

}
