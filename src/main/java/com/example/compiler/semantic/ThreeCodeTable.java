package com.example.compiler.semantic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName SymbolTable.java
 * @Description TODO
 * @createTime 2021年06月04日 17:13:00
 */
@SuppressWarnings("all")
public class ThreeCodeTable {
    private final HashMap<String, Variable> tableMap = new HashMap<>();

    public HashMap<String, Variable> getTableMap() {
        return tableMap;
    }

    public void putSingleSymbol(int id, String op, String arg1, String arg2, String res) {
        ThreeCodeTable.Variable variable = new ThreeCodeTable.Variable(op, arg1, arg2, res);
        tableMap.put(String.valueOf(id), variable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Variable> entry : tableMap.entrySet()) {
            System.out.println(entry.getKey() + ",");
            Variable variable = entry.getValue();
            System.out.println(variable.toString());
        }
        return sb.toString();
    }

    static class Variable {
        String op;
        String arg1;
        String arg2;
        String res;

        public Variable(String op, String arg1, String arg2, String res) {
            this.op = op;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.res = res;
        }

        @Override
        public String toString() {
            return "Variable{" +
                    "op='" + op + '\'' +
                    ", arg1='" + arg1 + '\'' +
                    ", arg2='" + arg2 + '\'' +
                    ", res='" + res + '\'' +
                    '}';
        }
    }
}
