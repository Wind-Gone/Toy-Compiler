package com.example.compiler.controller;


import com.example.compiler.lexer.Lexer;
import com.example.compiler.llParser.LLParser;
import com.example.compiler.llParser.LLUtil;
import com.example.compiler.token.Text;
import com.example.compiler.token.Token;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4343")
public class controller {
    @PostMapping("/lexer")
    public String getLexer(@RequestBody(required = false) Text text) {
        if (text == null || text.getSource().equals("") || text.getSource() == null)
            return "您此时的输入为空";
        String input = text.getSource();
        System.out.println(input);
        Lexer lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        StringBuilder res = new StringBuilder();
        for (Token token : tokens) {
            res.append(token);
            res.append("\n");
        }
        System.out.println(res);
        return res.toString();
    }

    @PostMapping("/grammer")
    public String getGrammerResult(@RequestBody Text text) {
        System.out.println(text.getSource());
        if (text.getSource().equals("") || text.getSource() == null)
            return "您此时的输入为空";
        String input = text.getSource();
        System.out.println(input);
        Lexer lexer = new Lexer();
        System.out.println("--------语法开始 ------");
        LLParser llParser = new LLParser(input);
        return "";
    }

    @GetMapping("/FirstSet")
    public List<String> getFirstSet() {
        LLUtil llUtil = new LLUtil();
        return llUtil.printFIrstSet();
    }

    @GetMapping("/FollowSet")
    public List<String> getFollowSet() {
        LLUtil llUtil = new LLUtil();
        return llUtil.printFollowSet();
    }

    @GetMapping("/finalTable")
    public String getFinalTable() {
        LLUtil llUtil = new LLUtil();
        String[][] finalTable = llUtil.printParsingTable();
        finalTable[0][0] = "VT/T";
        System.out.println(Arrays.deepToString(finalTable));
        return Arrays.deepToString(finalTable);
    }
}
