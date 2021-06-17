package com.example.compiler.lexer;

import com.example.compiler.entity.token.Token;
import com.example.compiler.entity.token.TokenTable;
import com.example.compiler.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName Main.java
 * @Description TODO
 * @createTime 2021年06月07日 22:49:00
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/test.txt");
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        TokenTable tokenTable = new TokenTable();
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            if (!Lexer.getTokenType(token.getTokenType()).equals("errors"))         // 判断token的类型并赋值
                tokenTable.put(token);
            else
                System.out.println(token);
        }
        sb.append(tokenTable);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/testResult2.txt"));
        bufferedWriter.write(sb.toString());
        bufferedWriter.close();
    }
}
