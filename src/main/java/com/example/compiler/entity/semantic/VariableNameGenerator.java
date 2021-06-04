package com.example.compiler.entity.semantic;

/**
 * 变量名产生器
 */
public class VariableNameGenerator {

    private static final String VAR_PREFIX = "T"; // 前缀
    public static int sequenceId = 0; // 序号 T1、T2、T3...

    public static String genVariableName() {
        ++sequenceId;
        return VAR_PREFIX + sequenceId;
    }

    public void clear() {
        sequenceId = 0;
    }
}
