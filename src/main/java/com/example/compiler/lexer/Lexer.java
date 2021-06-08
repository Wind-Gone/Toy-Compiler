package com.example.compiler.lexer;

import com.example.compiler.entity.token.Token;
import com.example.compiler.entity.token.TokenType;
import com.example.compiler.entity.wrong.ErrorCode;
import com.example.compiler.entity.wrong.WrongMessage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class Lexer {

    private final LinkedHashMap<TokenType, String> regEx;
    private final List<Token> result;
    private final IdentityHashMap<Pair<Integer, Integer>, WrongMessage> wrongList;
    private WrongMessage wrongMessage;
    private int row = 1;
    private int column = 1;

    public IdentityHashMap<Pair<Integer, Integer>, WrongMessage> getWrongList() {
        return wrongList;
    }


    /**
     * 根据属性值判断token类型
     *
     * @param value 属性值
     * @return token类型字符串
     */
    public static String getTokenType(TokenType tokenType) {
        if (tokenType == TokenType.IF || tokenType == TokenType.WHILE || tokenType == TokenType.ELSE || tokenType == TokenType.THEN)
            return "keywords";
        else if (tokenType == TokenType.IDENTIFIERS)
            return "identifiers";
        else if (tokenType == TokenType.DIGIT || tokenType == TokenType.INTNUMBER || tokenType == TokenType.EXPONENT || tokenType == TokenType.FRACTION || tokenType == TokenType.REALNUMBER)
            return "numbers";
        else if (tokenType == TokenType.PLUS || tokenType == TokenType.MINUS || tokenType == TokenType.DIVIDE || tokenType == TokenType.MULTIPLY || tokenType == TokenType.EQUAL || tokenType == TokenType.EQUALEQUAL || tokenType == TokenType.LESS || tokenType == TokenType.LESSEQUAL || tokenType == TokenType.GREATER || tokenType == TokenType.GREATEREQUAL)
            return "operators";
        else if (tokenType == TokenType.CLOSEBRACE || tokenType == TokenType.COMMA || tokenType == TokenType.OPENBRACE || tokenType == TokenType.CLOSECURLYBRACE || tokenType == TokenType.OPENCURLYBRACE || tokenType == TokenType.SEMICOLON)
            return "delimiters";
        return "errors";
    }


    public Lexer() {
        regEx = new LinkedHashMap<>();
        launchRegex();
        result = new ArrayList<>();
        wrongList = new IdentityHashMap<>();
    }

    public Lexer(String fileContent) {
        regEx = new LinkedHashMap<>();
        launchRegex();
        result = new ArrayList<>();
        wrongList = new IdentityHashMap<>();
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
            boolean expFlag = true;
            boolean intFlag = true;
            token = separateToken(source, position);
            if (token != null) {
                String digitNumber = "";
                if (token.getTokenType() == TokenType.EXPONENT || token.getTokenType() == TokenType.REALNUMBER) {
                    String tokenContent = token.getTokenString();
                    Pattern pattern = Pattern.compile("(\\d+)?([Ee]([\\+\\-]?)(\\d+))"); // 匹配形如10e+100 或e-100这种字符串
                    Matcher matcher = pattern.matcher(tokenContent);
                    if (matcher.matches()) {
                        //System.out.println(token.getTokenString());
                        int index = tokenContent.indexOf('e');
                        if (index == -1)        // 没查到小e
                            index = tokenContent.indexOf('E');
                        tokenContent = tokenContent.substring(index);
                        for (int i = 0; i < tokenContent.length(); i++) {
                            if (Character.isDigit(tokenContent.charAt(i))) {
                                digitNumber += tokenContent.charAt(i);
                            }
                        }
                        BigInteger a = new BigInteger(digitNumber);
                        BigInteger b = new BigInteger("128");
                        if (a.compareTo(b) == 1) {
                            expFlag = false;
                            wrongMessage = new WrongMessage(token.getTokenString(), ErrorCode.EXPONENT_GREATER_LIMIT);
                            wrongList.put(new Pair<>(token.getRow(), token.getColumn()), wrongMessage);
                            System.out.println(wrongList + "\n");
                        }
                    }
                } else if (token.getTokenType() == TokenType.INTNUMBER) {
                    System.out.println("666    " + token.getTokenString());
                    String tokenContent = token.getTokenString();
                    for (int i = 0; i < tokenContent.length(); i++) {
                        if (Character.isDigit(tokenContent.charAt(i))) {
                            digitNumber += tokenContent.charAt(i);
                        }
                    }
                    BigInteger a = new BigInteger(digitNumber);
                    BigInteger b = BigDecimal.valueOf(Math.pow(2, 31)).toBigInteger();
                    if (a.compareTo(b) == 1) {
                        intFlag = false;
                        wrongMessage = new WrongMessage(token.getTokenString(), ErrorCode.INT_GREATER_LIMIT);
                        wrongList.put(new Pair<>(token.getRow(), token.getColumn()), wrongMessage);
                    }
                }
                position = token.getEndIndex();
                if (expFlag && intFlag)
                    result.add(token);
            }
            if (token == null)
                position++;
        } while (position != source.length());
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
            // NUM EPSILON DOLLAR 不识别
            if (tokenType == TokenType.NUM || tokenType == TokenType.EPSILON || tokenType == TokenType.DOLLAR)
                continue;
//            System.out.println(tokenType);
            Pattern p = Pattern.compile(".{" + fromIndex + "}" + regEx.get(tokenType),
                    Pattern.DOTALL);
            Matcher m = p.matcher(source);
            if (m.matches()) {
                String lexema = m.group(1);
//                System.out.println("---------matched-----------");
                //找到回车，注释更新行和列
                if (tokenType == TokenType.ENTER || tokenType == TokenType.COMMENTS) {
                    row++;
                    int t = column;
                    column = 1;
                    return new Token(fromIndex, fromIndex + lexema.length(), row - 1, t, tokenType, lexema);
                }
                int t = column;
                column += lexema.length();
                //System.out.println(lexema+" "+String.valueOf(source.charAt(fromIndex)));
                return new Token(fromIndex, fromIndex + lexema.length(), row, t, tokenType, lexema);
            }
        }
        int position_row = row;
        int position_col = column + 1;
        column++;
        wrongMessage = new WrongMessage(String.valueOf(source.charAt(fromIndex)), ErrorCode.NOT_MATCH);
        wrongList.put(new Pair<>(position_row, position_col), wrongMessage);
        System.out.println(wrongList + "\n");
        return null;
    }

    /**
     * 获取token列表
     *
     * @return List<Token>
     */
    public List<Token> getTokens() {
        return result;
    }

    /**
     * 获取除空格tab回车之外的token列表
     *
     * @return List<Token>
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
        /*
         * 注释
         */
        regEx.put(TokenType.COMMENTS, "(//(.*?)(\\r)?\\n).*");

        /*
         * keywords
         */
        regEx.put(TokenType.IF, "\\b(if)\\b.*");
        regEx.put(TokenType.THEN, "\\b(then)\\b.*");
        regEx.put(TokenType.ELSE, "\\b(else)\\b.*");
        regEx.put(TokenType.WHILE, "\\b(while)\\b.*");

        /*
         * IDENTIFIERS
         */
        regEx.put(TokenType.IDENTIFIERS, "\\b([a-zA-Z]{1}[0-9a-zA-Z]{0,63})\\b.*");

        /*
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

        /*
         * delimiters
         */
        regEx.put(TokenType.OPENBRACE, "(\\().*");
        regEx.put(TokenType.CLOSEBRACE, "(\\)).*");
        regEx.put(TokenType.OPENCURLYBRACE, "(\\{).*");
        regEx.put(TokenType.CLOSECURLYBRACE, "(\\}).*");
        regEx.put(TokenType.SEMICOLON, "(\\;).*");
        regEx.put(TokenType.COMMA, "(\\,).*");

        /*
         * numbers
         */
        regEx.put(TokenType.DIGIT, "\\b(\\d{1})\\b.*");
        regEx.put(TokenType.INTNUMBER, "\\b(\\d+)\\b.*");
        regEx.put(TokenType.EXPONENT, "\\b([Ee]([\\+\\-]?)(\\d+))\\b.*");
        regEx.put(TokenType.FRACTION, "\\b(\\.\\d+)\\s.*");
        regEx.put(TokenType.REALNUMBER, "\\b((\\d+([Ee]([\\+\\-]?)(\\d+)))|(\\d+(\\.\\d+)([Ee]([\\+\\-]?)(\\d+))?))\\b.*");

        /*
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
        int ResultCode = inputStream.read(bytes);
        inputStream.close();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
