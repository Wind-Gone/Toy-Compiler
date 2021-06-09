package com.example.compiler.intermediateCodeGeneration;

public class Node extends Object{
    private int leftLeaf;
    private int rightLeaf;
    private String symbol;
    private int id;//自身pi
    private int firstEqualId;//实际与pi相等

    public Node(String symbol,int leftLeaf,int rightLeaf){
        this.symbol = symbol;
        this.leftLeaf = leftLeaf;
        this.rightLeaf = rightLeaf;
        this.id = -1;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", op='" + symbol + '\'' +
                ", arg1='" + leftLeaf + '\'' +
                ", arg2='" + rightLeaf + '\'' +
                '}';
    }
}
