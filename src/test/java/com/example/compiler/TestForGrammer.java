package com.example.compiler;

import com.example.compiler.entity.wrong.WrongMessage;
import com.example.compiler.llParser.LLParser;
import com.example.compiler.utils.FileUtils;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
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
    // 词法分析器
    private LLParser llParser;      //语法分析器
    private HashMap<Pair<Integer, Integer>, WrongMessage> wrongList;    //错误信息列表

    @BeforeAll
    public void readFile() throws IOException {
        //文件内容
        String content = FileUtils.ReadFile("src/testForLLParser.txt");
    }

    @ParameterizedTest(name = "测试检查样例")
    @ValueSource(strings = "{ sum=2+i+6; }\n")
    public void testCorrectProgram(String input) {
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
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        wrongList = llParser.getWrongList();
        Assertions.assertTrue(wrongList.toString().contains("NO_MATCH_IFELSE_WRONG"));      // 包含了if-else不匹配的报错
    }

    @ParameterizedTest(name = "测试缺少结束闭合花括号的样例")
    @ValueSource(strings = "{ while (i>0) {{n=n+1;i=i-1;} }\n")
    public void testWhileProgram(String input) {
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        wrongList = llParser.getWrongList();
        Assertions.assertTrue(wrongList.toString().contains("MISS_END_CLOSECURLYBRACES"));      // 包含了括号不匹配的报错
    }

    @ParameterizedTest(name = "测试操作符误写情况的报错处理")
    @ValueSource(strings = "{ a = * 1;  }\n")
    public void testNotReasonableSymbolProgram(String input) {
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        wrongList = llParser.getWrongList();
        Assertions.assertTrue(wrongList.toString().contains("NOT_REASONABLE_SYMBOL"));
    }

    @ParameterizedTest(name = "测试等号误写情况的报错处理")
    @ValueSource(strings = "{ a === 1;  }\n")
    public void testEqualProgram(String input) {
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        wrongList = llParser.getWrongList();
        Assertions.assertTrue(wrongList.toString().contains("EXTRA_EQUAL"));
    }

    @ParameterizedTest(name = "测试分号误写情况的报错处理")
    @ValueSource(strings = "{ a = 1;;  }\n")
    public void testExtraSemicolonProgram(String input) {
        llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        wrongList = llParser.getWrongList();
        Assertions.assertTrue(wrongList.toString().contains("EXTRA_SEMICOLON"));
    }


    @ParameterizedTest(name = "测试缺失右括号或多余的左括号情况")
    @ValueSource(strings = "{  a = 1; if (a>0)) then a=a+1; else{a=a-1;  }\n")
    public void testMissProgram(String input) {
        llParser = new LLParser(input);
        System.out.println("******");
        wrongList = llParser.getWrongList();
        Assertions.assertTrue(wrongList.toString().contains("MISS_OR_EXTRA_OPENBRACE"));
    }
}
