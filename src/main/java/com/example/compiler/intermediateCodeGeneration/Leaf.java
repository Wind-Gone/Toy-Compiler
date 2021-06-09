package com.example.compiler.intermediateCodeGeneration;

import com.example.compiler.entity.token.TokenType;

public class Leaf extends Object{
    private TokenType tokenType;//token类型，是id或num
    private int id;//自身pi
    private int firstEqualId;//实际与pi相等
    private int no;//num值
    private String entry;//变量名称
    private boolean isNum;
    //num类型
    public Leaf(TokenType tokenType,int no){
        this.isNum = true;
        this.tokenType = tokenType;
        this.id = -1;
        this.no = no;

    }
    //id类型
    public Leaf(TokenType tokenType,String entry){
        this.isNum = false;
        this.tokenType = tokenType;
        this.id = -1;
        this.no = -1;
        this.entry = entry;

    }
    public void setId(int id){
        this.id = id;
    }
    public void setFirstEqualId(int firstEqualId){
        this.firstEqualId = firstEqualId;
    }
    public int getId(){
        return this.id;
    }

    public String toString() {
        return "Leaf{" +
                "id='" + id + '\'' +
                ", TokenType='" + String.valueOf(tokenType) + '\'' +
                ", entry='" + entry + '\'' +
                ", no ='" + no + '\'' +
                '}';
    }


}
