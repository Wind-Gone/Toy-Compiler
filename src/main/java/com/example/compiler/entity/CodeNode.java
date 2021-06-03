package com.example.compiler.entity;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName CodeNode.java
 * @Description TODO
 * @createTime 2021年06月02日 23:10:00
 */
public class CodeNode {
    private int line;
    private String opt;
    private String arg1, arg2;
    private String result;

    public CodeNode(int line, String opt, String arg1, String arg2, String result) {
        this.line = line;
        this.opt = opt;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
