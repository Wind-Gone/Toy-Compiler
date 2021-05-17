package com.example.compiler.controller;


import com.example.compiler.lexer.Lexer;
import com.example.compiler.token.Text;
import com.example.compiler.token.Token;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:1212")
public class controller {
    @PostMapping("/lexer")
    public String getLexer(@RequestBody Text text) {
        String input = text.getSource();
        System.out.println(input);
//        input = URLDecoder.decode(input,"UTF-8");
//        System.out.println(input);
        Lexer lexer = new Lexer();
        lexer.tokenize(input);
        List<Token> tokens = lexer.getFilteredTokens();
        StringBuilder res = new StringBuilder();
        for (Token token : tokens) {
            res.append(token);
            res.append("\n");
        }
        System.out.println(res.toString());
        return res.toString();
    }
}
