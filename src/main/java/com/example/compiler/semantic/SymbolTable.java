package com.example.compiler.semantic;

import java.util.HashMap;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName SymbolTable.java
 * @Description TODO
 * @createTime 2021年06月04日 17:13:00
 */
@SuppressWarnings("all")
public class SymbolTable {
    private final HashMap<String, Variable> tableMap = new HashMap<>();
    private int number = 1;

    public HashMap<String, Variable> getTableMap() {
        return tableMap;
    }

    public void putSingleSymbol(String name, Number val) {
        Variable variable = new Variable();
        variable.name = name;
        variable.val = val;
        variable.size = 4;
        variable.level = 1;
        variable.type = "RealNumber";
        tableMap.put(variable.name, variable);
    }

    public Number getVal(String name) throws Exception {
        Variable variable = tableMap.get(name);
        if (variable != null)
            return variable.val;
        else {
            return Integer.MAX_VALUE;
        }
    }

    public void setVal(String name, Number val) throws Exception {
        Variable variable = tableMap.get(name);
        if (variable != null)
            variable.val = val;
        else
            throw new Exception("该变量不存在,无法更新其值");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Variable variable : tableMap.values()) {
            sb.append(variable.name).append(":").append(variable.val).append("\n");
        }
        return sb.toString();
    }

    public String PrintAll() {
        StringBuilder sb = new StringBuilder();
        for (Variable variable : tableMap.values()) {
            sb.append(variable.name).append(" ").append(variable.val).append(variable.type).append(" ").append(variable.size).append(" ").append(variable.level).append(" ").append("\n");
        }
        return sb.toString();
    }

    static class Variable {         // 符号表中的变量
        String name;
        Number val;
        String type;
        Integer size;
        int level;

        @Override
        public String toString() {
            return "Variable:" + name + "=" + val;
        }

        public String PrintAll() {
            return name + " " + val + " " + type + " " + size + " " + level;
        }
    }
}
