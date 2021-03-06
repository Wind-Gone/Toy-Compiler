package com.example.compiler;

import com.example.compiler.entity.gui.GuiNode;
import com.example.compiler.entity.token.Token;
import com.example.compiler.entity.token.TokenType;
import com.example.compiler.entity.tree.TreeNode;
import com.example.compiler.entity.wrong.WrongMessage;
import com.example.compiler.intermediateCodeGeneration.Dag;
import com.example.compiler.lexer.Lexer;
import com.example.compiler.llParser.LLParser;
import com.example.compiler.llParser.LLUtil;
import com.example.compiler.llParser.NonTerminalType;
import com.example.compiler.semantic.SemanticAnalyzer;
import com.example.compiler.utils.FileUtils;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SpringBootTest
@SuppressWarnings("all")
class CompilerApplicationTests {
    @Test
    void contextLoads() throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/2.txt");
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        StringBuilder res = new StringBuilder();
        for (Token token : tokens) {
            res.append(token);
            res.append("\n");
        }
        System.out.println(res.toString());
        for (Map.Entry<Pair<Integer, Integer>, WrongMessage> entry : lexer.getWrongList().entrySet()) {
            Pair<Integer, Integer> resultPair = entry.getKey();
            int row = resultPair.getKey();
            int col = resultPair.getValue();
            System.out.println("行：" + row + "， 列：" + col + "，此处的字符串\"" + entry.getValue().getTokenContent() + "\"附近或许存在错误，提示：" +
                    entry.getValue().getErrorCode().getMessage());
        }

    }

    @Test
    void czh() {
        List<Object> list = new ArrayList<Object>() {{
            add(TokenType.DIVIDE);
            add(NonTerminalType.WHILESTMT);
        }};
        for (Object i : list) {
            if (i instanceof TokenType) {
                System.out.println(i);
            }
        }
    }

    @Test
    void czh2() {
        LLUtil llUtil = new LLUtil();
        String[][] res = llUtil.printParsingTableView();
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                System.out.printf("%13s", res[i][j]);
            }
            System.out.println();
        }
        System.out.printf("%10s %10s", "aa", "bb");
    }

    @Test
    void czh3() throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/3.txt");
        System.out.println("--------语法开始 ------");
        LLParser llParser = new LLParser(input);
        GuiNode tree = llParser.printGuiNode();
    }

    @Test
    void testFirstSet() throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/3.txt");
        System.out.println("--------语法开始 ------");
        LLParser llParser = new LLParser(input);
        System.out.println("******");
    }

    @Test
    void testSynTree() throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/3.txt");
        System.out.println("--------语法开始 ------");
        LLParser llParser = new LLParser(input);
        System.out.println("******");
        List<String> a = llParser.getSyntaxTree().dfs();
        for (String s : a) {
            System.out.println(s + " ");
        }
    }

    @Test
    void testDag() throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/3.txt");
        LLParser llParser = new LLParser(input);
        System.out.println("--------中间代码生成 ------");
        TreeNode root = llParser.getSyntaxTree().getRoot();
        Dag dag = new Dag(root);
        dag.assgDag();
    }

    @Test
    void testPrint() throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/2.txt");
        System.out.println("--------语法开始 ------");
        LLParser llParser = new LLParser(input);
        llParser.getSyntaxTree().preOrderPrint();
    }


    @Test
    /**
     * @Description
     * @Author Hu Zirui
     * @Throws
     */
    void testFinalTable() throws IOException {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/3.txt");
        System.out.println("--------语法开始 ------");
        LLParser llParser = new LLParser(input);
        System.out.println("********");
//        System.out.println(res.toString());
    }


    /**
     * 语义分析测试
     */
    @Test
    public void testForSemantic() throws Exception {
        Lexer lexer = new Lexer();
        String input = FileUtils.ReadFile("src/2.txt");
        System.out.println("--------语义开始 ------");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(input);
        String output = semanticAnalyzer.toString();
        System.out.println(output);
    }
}
