package com.example.compiler.entity.token;

public class Token {
    private int beginIndex;
    private int endIndex;
    private int row;
    private int column;
    private TokenType tokenType;
    private String tokenString;

    @Deprecated
    public Token(int beginIndex, int endIndex, TokenType tokenType, String tokenString) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.tokenType = tokenType;
        this.tokenString = tokenString;
    }

    public Token(TokenType tokenType){
        this.tokenType = tokenType;
    }

    public Token(int beginIndex, int endIndex, int row, TokenType tokenType, String tokenString) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.row = row;
        this.tokenType = tokenType;
        this.tokenString = tokenString;
    }

    public Token(int beginIndex, int endIndex, int row, int column, TokenType tokenType, String tokenString) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.row = row;
        this.column = column;
        this.tokenType = tokenType;
        this.tokenString = tokenString;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token{" +
                "beginIndex=" + beginIndex +
                ", endIndex=" + endIndex +
                ", row=" + row +
                ", column=" + column +
                ", tokenType=" + tokenType +
                ", tokenString='" + tokenString + '\'' +
                '}';
    }
}
