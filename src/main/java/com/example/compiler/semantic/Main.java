package com.example.compiler.semantic;

import com.example.compiler.utils.FileUtils;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName Main.java
 * @Description TODO
 * @createTime 2021年06月10日 00:58:00
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String input = FileUtils.ReadFile("src/3.txt");
        System.out.println("--------语义开始 ------");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(input);
        String output = semanticAnalyzer.toString();
        System.out.println(output);
    }
}
