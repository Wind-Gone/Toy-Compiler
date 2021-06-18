package com.example.compiler.entity.token;

import com.example.compiler.lexer.Lexer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName TokenTable.java
 * @Description TODO
 * @createTime 2021年06月08日 23:08:00
 */
public class TokenTable {
    private final HashMap<Integer, TokenTable.Variable> tableMap = new HashMap<>();
    private int number = 0;

    public void put(Token token) {
        TokenTable.Variable variable = new TokenTable.Variable(token.getRow(), token.getColumn(), token.getTokenString(), Lexer.getTokenType(token.getTokenType()), token.getTokenType());
        tableMap.put(++number, variable);
    }

    public Variable get(int id) {
        return tableMap.get(id);
    }

    public void setNewVal(int id, String val) {
        Variable variable = tableMap.get(id);
        variable.name = val;
        tableMap.put(id, variable);
    }

    @Override
    public String toString() {
        String header = "Id TokenType SpecificType TokenName RowNumber ColNumber\n";
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        for (Map.Entry<Integer, Variable> variable : tableMap.entrySet()) {
            sb.append(variable.getKey()).append(" ").append(variable.getValue().type).append(" ").append(variable.getValue().detailType).append(" ").append(variable.getValue().name).append(" ").append(variable.getValue().rowNumber).append(" ").append(variable.getValue().colNumber).append("\n");
        }
        return sb.toString();
    }

    static class Variable {         // 符号表中的变量
        Integer rowNumber;
        Integer colNumber;
        String name;
        String type;
        TokenType detailType;

        public Variable(Integer rowNumber, Integer colNumber, String name, String type, TokenType detailType) {
            this.rowNumber = rowNumber;
            this.colNumber = colNumber;
            this.name = name;
            this.type = type;
            this.detailType = detailType;
        }
    }
}
