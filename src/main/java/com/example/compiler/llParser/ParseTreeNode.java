package com.example.compiler.llParser;

import com.example.compiler.token.TokenType;

import java.util.LinkedList;
import java.util.List;

public class ParseTreeNode {
    private boolean isToken;//判断是不是token
    private boolean notRoot;//判断是不是根节点
    private boolean used;
    private int id = -1;
    private NonTerminalType NonTerminalExpression;
    private TokenType TokenTypeExpression;
    private List<ParseTreeNode> successors;//后继节点


    //该节点为非终结符
    public ParseTreeNode(NonTerminalType NonTerminalExpression) {
        this.NonTerminalExpression = NonTerminalExpression;
        this.isToken = false;
        this.notRoot = true;
        this.successors = new LinkedList<>();
        this.used = false;
    }
    //该节点为终结符,无后继节点
    public ParseTreeNode(TokenType TokenTypeExpression){
        this.TokenTypeExpression = TokenTypeExpression;
        this.isToken = true;
        this.notRoot = true;
        this.successors = new LinkedList<>();
        this.used = false;

    }
    public void setRoot(){this.notRoot = false;}
    public void setUsed(){this.used = true;}
    public void setId(int id){this.id = id;}
    public void addSuccessors(ParseTreeNode sucessor){
        successors.add(sucessor);
    }

    public boolean getIsToken(){return isToken;}
    public boolean getNotRoot(){return notRoot;}
    public boolean getUsed(){return used;}


    public NonTerminalType getNonTerminalExpression(){return NonTerminalExpression;}
    public TokenType getTokenTypeExpression(){return TokenTypeExpression;}

    public List<ParseTreeNode> getSuccessors(){return successors;}




}
