package com.example.compiler;

import com.example.compiler.lexer.Lexer;
import com.example.compiler.llParser.LLParser;
import com.example.compiler.utils.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName TestForGrammer.java
 * @Description TODO
 * @createTime 2021年06月10日 22:46:00
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestForGrammer {
    private Lexer lexer;            // 词法分析器
    private LLParser llParser;      //语法分析器

    @BeforeAll
    public void readFile() throws IOException {
        //文件内容
        String content = FileUtils.ReadFile("src/testForLLParser.txt");
    }

    @ParameterizedTest(name = "测试检查样例")
    @ValueSource(strings = "{ sum=2+i+6; }\n")
    public void testCorrectProgram(String input) {
        lexer = new Lexer();
        System.out.println("--------语法开始 ------");
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        for (String s : a) {
            System.out.println(s + " ");
        }
    }

    @ParameterizedTest(name = "测试if-else不匹配样例")
    @ValueSource(strings = "{ if (n>1) then n=n-1; else {n=n+1;} else {n=n+2;} }\n")
    public void testIfProgram(String input) {
        lexer = new Lexer();
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        for (String s : a) {
            System.out.println(s + " ");
        }
    }

    @ParameterizedTest(name = "测试包含while括号不匹配样例")
    @ValueSource(strings = "{ while (i>0) {{n=n+1;i=i-1;} }\n")
    public void testWhileProgram(String input) {
        lexer = new Lexer();
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        for (String s : a) {
            System.out.println(s + " ");
        }
    }

    @ParameterizedTest(name = "测试包含while正确样例")
    @ValueSource(strings = "{ while (i>0) {n=n+1;i=i-1;} i=i+2; }\n")
    public void testWhileCorrectProgram(String input) {
        lexer = new Lexer();
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        for (String s : a) {
            System.out.println(s + " ");
        }
    }


}
