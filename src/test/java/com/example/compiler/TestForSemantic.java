package com.example.compiler;

import com.example.compiler.entity.wrong.WrongMessage;
import com.example.compiler.semantic.SemanticAnalyzer;
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
public class TestForSemantic {
    private SemanticAnalyzer semanticAnalyzer;            // 语义分析器

    private IdentityHashMap<Pair<Integer, Integer>, WrongMessage> wrongList;    //错误信息列表
    private String content = "";        //文件内容

    @BeforeAll
    public void readFile() throws IOException {
        content = FileUtils.ReadFile("src/testForSemantic.txt");
    }

    @ParameterizedTest(name = "测试词法分析的正确样例")
    @ValueSource(strings = "{\n" +
            "i=10;\n" +
            "j=100;\n" +
            "n=1;\n" +
            "sum=0;\n" +
            "mult=1;\n" +
            "while (i>0) {n=n+1;i=i-1;}\n" +
            "if (j>=50) then sum=sum+j; else {mult=mult*(j+1);sum=sum+i;}\n" +
            "if (i<=10) then sum=sum-i; else {mult=mult+i/2;}\n" +
            "if (i==j) then sum=sum-j; else {mult=mult-j/2;}\n" +
            "if (n>1) then n=n-1; else {n=n+1;}\n" +
            "if (n<2) then n=n+2; else {n=n-2;}\n" +
            "}\n")
    public void testCorrectProgram1(String input) throws Exception {
        semanticAnalyzer = new SemanticAnalyzer(input);
        String output = semanticAnalyzer.toString();
        System.out.println(output);
        Assert.assertTrue(output.contains("mult:-49"));
        Assert.assertTrue(output.contains("i:0"));
        Assert.assertTrue(output.contains("j:100"));
        Assert.assertTrue(output.contains("n:8"));
        Assert.assertTrue(output.contains("sum:100"));
    }

}
