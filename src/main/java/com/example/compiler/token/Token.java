package com.example.compiler.token;

public class Token {
    private int beginIndex;
    private int endIndex;
    private TokenType tokenType;
    private String tokenString;

    public Token(int beginIndex, int endIndex, TokenType tokenType, String tokenString) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
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

    @Override
    public String toString() {
        return "Token{" +
                "beginIndex=" + beginIndex +
                ", endIndex=" + endIndex +
                ", tokenType=" + tokenType +
                ", tokenString='" + tokenString + '\'' +
                '}';
    }
}