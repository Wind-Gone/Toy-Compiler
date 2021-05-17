package com.example.compiler;

import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Token;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
		System.out.println(res.toString());
	}
}
