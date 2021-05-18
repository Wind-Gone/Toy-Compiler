package com.example.compiler;

import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Token;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CompilerApplicationTests {
    @Test
    void contextLoads() throws IOException {
        Lexer lexer = new Lexer();
        String input = lexer.ReadFile("src/2.txt");
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        StringBuilder res = new StringBuilder();
        for (Token token : tokens) {
            res.append(token);
            res.append("\n");
        }
//		System.out.println(res.toString());
        for (Map.Entry<Pair<Integer, Integer>, String> entry : lexer.getWrongList().entrySet()) {
            Pair<Integer, Integer> resultPair = entry.getKey();
            int row = resultPair.getKey();
            int col = resultPair.getValue();
            System.out.println("行：" + row + "， 列：" + col + "我们无法识别此处的字符串\"" + entry.getValue() + "\"，请您检测您此处的程序是否有误");
        }
    }
}
