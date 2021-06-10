package com.example.compiler.semantic;

import com.example.compiler.llParser.LLParser;
import com.example.compiler.utils.FileUtils;

import java.util.List;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName Main.java
 * @Description TODO
 * @createTime 2021年06月10日 00:58:00
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String input = FileUtils.ReadFile("src/test.txt");
        System.out.println("--------语义开始 ------");
        LLParser llParser = new LLParser(input);
        List<String> a = llParser.getSyntaxTree().dfs();
        for (String s : a) {
            System.out.println(s + " ");
        }
        System.out.println("******");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(input);
        String output = semanticAnalyzer.toString();
        System.out.println(output);
    }
}
