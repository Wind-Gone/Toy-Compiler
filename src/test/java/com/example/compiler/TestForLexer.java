package com.example.compiler;

import com.example.compiler.entity.token.Token;
import com.example.compiler.entity.wrong.WrongMessage;
import com.example.compiler.lexer.Lexer;
import com.example.compiler.utils.FileUtils;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName TestForLexer.java
 * @Description TODO
 * @createTime 2021年06月09日 11:03:00
 */
@SpringBootTest
@SuppressWarnings("all")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestForLexer {
    private Lexer lexer;            // 词法分析器

    private IdentityHashMap<Pair<Integer, Integer>, WrongMessage> wrongList;    //错误信息列表
    private String content = "";        //文件内容

    @BeforeAll
    public void readFile() throws IOException {
        content = FileUtils.ReadFile("src/testForLexer.txt");
    }

    @ParameterizedTest(name = "测试词法分析的正确样例1")
    @ValueSource(strings = "{ if(hu)then i = 9; }\n")
    public void testCorrectProgram1(String input) {
        int TokenNumber = 11;
        lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        wrongList = lexer.getWrongList();
        Assert.assertEquals(wrongList.toString(), "{}");              // 正确运行，错误列表应该为空
        Assert.assertEquals(TokenNumber, tokens.size());                   // 正确运行，分词大小相等
    }

    @ParameterizedTest(name = "测试词法分析的正确样例2")
    @ValueSource(strings = "{ i =  10;\n" +
            "j = 100;\n" +
            "n=1;\n" +
            "sum=0;\n" +
            "mult=1;\n" +
            "while (i>0) {n=n+1;i=i-1;}\n" +
            "if (j>=50) then sum=sum+j; else {mult=mult*(j+1);sum=sum+i;}\n" +
            "if (i<=10) then sum=sum-i; else {mult=mult+i/2;}\n" +
            "if (i ==j) then sum=sum-j; else {mult=mult-j/2;}\n" +
            "if (n>1) then n=n-1; else {n=n+1;}\n" +
            "if (n<2) then n=n+2; else {n=n-2;} }\n")
    public void testCorrectProgram2(String input) {
        int TokenNumber = 166;
        lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        wrongList = lexer.getWrongList();
        Assert.assertEquals(wrongList.toString(), "{}");              // 正确运行，错误列表应该为空
        Assert.assertEquals(TokenNumber, tokens.size());                   // 正确运行，分词大小相等
    }


    @ParameterizedTest(name = "测试词法分析的正确样例3")
    @ValueSource(strings = "a=1;\n" +
            "sum=0;\n" +
            "mult=1;\n" +
            "while (i>0) {a=a+1;sum=sum+a;}")
    public void testCorrectProgram3(String input) {
        int TokenNumber = 32;
        lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        wrongList = lexer.getWrongList();
        Assert.assertEquals(wrongList.toString(), "{}");              // 正确运行，错误列表应该为空
        Assert.assertEquals(TokenNumber, tokens.size());                   // 正确运行，分词大小相等
    }


    @ParameterizedTest(name = "测试词法分析Intnumber值大于2^31的异常情况")
    @ValueSource(strings = "{a = 1111111111111111;}")
    public void testIntNumberGreaterThanMax(String input) {
        int TokenNumber = 11;
        lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        wrongList = lexer.getWrongList();
        Assert.assertTrue(wrongList.toString().contains("INT_GREATER_LIMIT"));      // 超过阈值成功报错
    }

    @ParameterizedTest(name = "测试词法分析指数值大于128的异常情况")
    @ValueSource(strings = "{a = E129;}")
    public void testExponentGreaterThanMax(String input) {
        lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        wrongList = lexer.getWrongList();
        Assert.assertTrue(wrongList.toString().contains("EXPONENT_GREATER_LIMIT"));      // 超过阈值成功报错
    }

    @ParameterizedTest(name = "测试词法分析RealNumber不规范的异常情况")
    @ValueSource(strings = "{a = 1E/129;}")
    public void testDigitNotStandard(String input) {
        lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        wrongList = lexer.getWrongList();
        Assert.assertTrue(wrongList.toString().contains("NOT_MATCH"));
    }
}
