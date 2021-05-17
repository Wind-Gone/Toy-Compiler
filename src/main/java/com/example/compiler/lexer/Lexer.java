package com.example.compiler.lexer;

import com.example.compiler.token.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private LinkedHashMap<TokenType, String> regEx;
    private List<Token> result;

    public Lexer() {
        regEx = new LinkedHashMap<TokenType, String>();
        launchRegex();
        result = new ArrayList<Token>();
    }

    /**
     * 入口
     *
     * @param source
     */
    public void tokenize(String source) {
        int position = 0;
        Token token = null;
        do {
            token = separateToken(source, position);
            if (token != null) {
                position = token.getEndIndex();
                result.add(token);
            }
        } while (token != null && position != source.length());
    }

    /**
     * 找到下一个token
     *
     * @param source
     * @param fromIndex
     * @return
     */
    private Token separateToken(String source, int fromIndex) {
        if (fromIndex < 0 || fromIndex >= source.length()) {
            throw new IllegalArgumentException("Illegal index in the input stream!");
        }
        for (TokenType tokenType : TokenType.values()) {
            System.out.println(tokenType);
            Pattern p = Pattern.compile(".{" + fromIndex + "}" + regEx.get(tokenType),
                    Pattern.DOTALL);
            Matcher m = p.matcher(source);
            if (m.matches()) {
                String lexema = m.group(1);
                return new Token(fromIndex, fromIndex + lexema.length(), tokenType, lexema);
            }
        }
        return null;
    }

    /**
     * 获取token列表
     *
     * @return
     */
    public List<Token> getTokens() {
        return result;
    }

    /**
     * 获取除空格tab回车之外的token列表
     *
     * @return
     */
    public List<Token> getFilteredTokens() {
        List<Token> filteredResult = new ArrayList<Token>();
        for (Token t : this.result) {
            if (!t.getTokenType().isAuxiliary()) {
                filteredResult.add(t);
            }
        }
        return filteredResult;
    }


    private void launchRegex() {
        /**
         * 注释
         */
        regEx.put(TokenType.COMMENTS, "(//(.*?)(\\r)?\\n).*");

        /**
         * keywords
         */
        regEx.put(TokenType.IF, "\\b(if)\\b.*");
        regEx.put(TokenType.THEN, "\\b(then)\\b.*");
        regEx.put(TokenType.ELSE, "\\b(else)\\b.*");
        regEx.put(TokenType.WHILE, "\\b(while)\\b.*");

        /**
         * IDENTIFIERS
         */
        regEx.put(TokenType.IDENTIFIERS, "\\b([a-zA-Z]{1}[0-9a-zA-Z]{0,63})\\b.*");

        /**
         * operator
         */
        regEx.put(TokenType.PLUS, "(\\+{1}).*");
        regEx.put(TokenType.MINUS, "(\\-{1}).*");
        regEx.put(TokenType.DIVIDE, "(/).*");
        regEx.put(TokenType.MULTIPLY, "(\\*).*");
        regEx.put(TokenType.EQUAL, "(=).*");
        regEx.put(TokenType.EQUALEQUAL, "(==).*");
        regEx.put(TokenType.LESS, "(<).*");
        regEx.put(TokenType.LESSEQUAL, "(<=).*");
        regEx.put(TokenType.GREATER, "(>).*");
        regEx.put(TokenType.GREATEREQUAL, "(>=).*");
        regEx.put(TokenType.NOTEQUAL, "(\\!=).*");

        /**
         * delimiters
         */
        regEx.put(TokenType.OPENBRACE, "(\\().*");
        regEx.put(TokenType.CLOSEBRACE, "(\\)).*");
        regEx.put(TokenType.OPENCURLYBRACE, "(\\{).*");
        regEx.put(TokenType.CLOSECURLYBRACE, "(\\}).*");
        regEx.put(TokenType.SEMICOLON, "(\\;).*");
        regEx.put(TokenType.COMMA, "(\\,).*");

        /**
         * numbers
         */
        regEx.put(TokenType.DIGIT, "\\b(\\d{1})\\b.*");
        regEx.put(TokenType.INTNUMBER, "\\b(\\d{1,9})\\b.*");
        regEx.put(TokenType.EXPONENT, "\\b([Ee]([\\+\\-]?)(\\d{1,9}))\\b.*");
        regEx.put(TokenType.FRACTION, "\\b(\\.\\d+)\\b.*");
        regEx.put(TokenType.REALNUMBER, "\\b( (\\d+([Ee]([\\+\\-]?)(\\d+))) | (\\d+(\\.\\d+)([Ee]([\\+\\-]?)(\\d+))?) )\\b.*");

        /**
         * 空格 回车 tab
         */
        regEx.put(TokenType.WHITESPACE, "( ).*");
        regEx.put(TokenType.NEWLINE, "(\\n).*");
        regEx.put(TokenType.TAB, "(\\t).*");
        regEx.put(TokenType.ENTER, "(\\r).*");
    }

    public String ReadFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
        int length = inputStream.available();
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        String str = new String(bytes, StandardCharsets.UTF_8);
        return str;
    }


}
