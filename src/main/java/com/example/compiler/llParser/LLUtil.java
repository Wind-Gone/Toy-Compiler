package com.example.compiler.llParser;

import com.example.compiler.token.Token;
import com.example.compiler.token.TokenType;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * czh First 集 Follow 集
 */
public class LLUtil {

    public HashMap<Object, Set<TokenType>> getFirstSet(){
        HashMap<Object, Set<TokenType>> res= new HashMap<>();
        Grammer grammer=new Grammer();
        int n=28;

        /*初始化 */
        for(NonTerminalType item : NonTerminalType.values()){
            res.put(item,new TreeSet<>());
        }

        /* 终结符的First集就是它本身 */
        for(int i=1;i<=n;i++){
            Production production = grammer.get(i);
            List<Object> right_expr = production.getRightExpression();
            for(Object object : right_expr){
                if (object instanceof  TokenType){
                    res.put(object,new TreeSet<TokenType>(){{
                        add((TokenType) object);
                    }});
                }
            }
        }

        /* 主循环 */
        boolean change = true;
        while (change) {
            change = false;
            for( int i=1;i<=n;i++ ) {
                Production production =grammer.get(i);
                List<Object> right_expr = production.getRightExpression();
                Set<TokenType> cur_FirstSet_Left = res.get(production.getLeftExpression());
                if(right_expr.get(0) instanceof TokenType){
                    if(!cur_FirstSet_Left.contains(right_expr.get(0))){
                        change = true;
                        cur_FirstSet_Left.add((TokenType) right_expr.get(0));
                    }
                }else {
                    boolean next = true;
                    int idx = 0;
                    while(next && idx < right_expr.size()) {
                        next = false ;
                        Object Y = right_expr.get(idx);
                        Set<TokenType> temp_FirstSet = res.get(Y);
                        for(TokenType item : temp_FirstSet){
                            if(item != TokenType.EPSILON){
                                if(!cur_FirstSet_Left.contains(item)){
                                    change = true ;
                                    cur_FirstSet_Left.add(item);
                                }
                            }
                        }
                        if(temp_FirstSet.contains(TokenType.EPSILON)){
                            next = true;
                            idx ++ ;
                        }
                    }
                }
            }
        }
        return res;
    }

    private Set<TokenType> getFirstSetForAlphas(HashMap<Object, Set<TokenType>> FirstSet, List<Object> alphas){
        Set<TokenType> ToAdd = new TreeSet<>();
        boolean next = true ;
        int idx = 0;
        while(idx < alphas.size() && next){
            next = false ;
            Object cur = alphas.get(idx);
            /* 当前符号是终结符或空，加入到FIRST集中 */
            if(cur instanceof  TokenType) {
                /* 判断是否已经在FIRST集中 */
                if( !ToAdd.contains(cur)){
                    ToAdd.add((TokenType) cur);
                }
            } else {
                Set<TokenType> curFirstSet = FirstSet.get(cur);
                for(TokenType token : curFirstSet) {
                    /* 当前符号FIRST集包含空，标记next为真，并跳过当前循环 */
                    if(token == TokenType.EPSILON){
                        next=true;
                        continue;
                    }
                    /* 把非空元素加入到FIRST集中 */
                    if(!ToAdd.contains(token)){
                        ToAdd.add(token);
                    }
                }
                idx++;
            }
            if (next){
                ToAdd.add(TokenType.EPSILON);
            }
        }
        return ToAdd;
    }

    public HashMap<Object, Set<TokenType>> getFollowSet() {
        HashMap<Object, Set<TokenType>> res= new HashMap<>();
        Grammer grammer=new Grammer();
        HashMap<Object, Set<TokenType>> FirstSet = getFirstSet();
        int n=28;

        /*初始化 非终结符 */
        for(NonTerminalType item : NonTerminalType.values()){
            res.put(item,new TreeSet<>());
        }

        /* 初始化 终结符 */
        for(int i=1;i<=n;i++){
            Production production = grammer.get(i);
            List<Object> right_expr = production.getRightExpression();
            for(Object object : right_expr){
                if (object instanceof  TokenType){
                    res.put(object,new TreeSet<TokenType>());
                }
            }
        }
        /* program 加 $ */
        res.get(NonTerminalType.PROGRAM).add(TokenType.DOLLAR);

        /* 主循环 */
        boolean change = true ;
        while (change) {
            change = false;
            for( int i=1;i<=n;i++ ) {
                Production production = grammer.get(i);
                List<Object> right_expr = production.getRightExpression();
                for(int j=0;j<right_expr.size();j++) {
                    Object item = right_expr.get(j);
                    if(item instanceof NonTerminalType) {
                        Set<TokenType> itemFollowSet = res.get(item);
                        List<Object> alphas = right_expr.subList(j+1,right_expr.size());
                        Set<TokenType> alphasFirstSet = getFirstSetForAlphas(FirstSet,alphas);
//                        System.out.println("j:  "+j);
//                        System.out.println("right_expr:   "+right_expr);
//                        System.out.println("alphas:  "+ alphas);
//                        System.out.println("alphasFirstSet:  "+ alphasFirstSet);
                        for(TokenType token : alphasFirstSet ) {
                            if(token == TokenType.EPSILON){
                                continue;
                            }
                            if(!itemFollowSet.contains(token)){
                                change = true;
                                itemFollowSet.add(token);
//                                if(item == NonTerminalType.MULTEXPR && token == TokenType.MULTIPLY ){
//                                    System.out.println("1111");
//                                    System.out.println(production);
//                                }
                            }
                        }
                        if(alphasFirstSet.contains(TokenType.EPSILON) || (j+1) >= right_expr.size() ) {
                            NonTerminalType left = production.getLeftExpression();
                            Set<TokenType> leftFollowSet = res.get(left);
                            for(TokenType token : leftFollowSet) {
                                if(!itemFollowSet.contains(token)){
                                    change = true ;
                                    itemFollowSet.add(token);

                                    if(item == NonTerminalType.MULTEXPR && token == TokenType.MULTIPLY ){
                                        System.out.println("2222");
                                        System.out.println(production);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;

    }

}
