package com.example.compiler.entity;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName WrongMessage.java
 * @Description TODO
 * @createTime 2021年05月18日 15:13:00
 */
public class WrongMessage {
    private String tokenContent;
    private ErrorCode errorCode;
    private String stage;


    public WrongMessage(String tokenContent, ErrorCode errorCode) {
        this.tokenContent = tokenContent;
        this.errorCode = errorCode;
        this.stage = "词法分析阶段";
    }

    public WrongMessage(String tokenContent, ErrorCode errorCode, String stage) {
        this.tokenContent = tokenContent;
        this.errorCode = errorCode;
        this.stage = stage;
    }

    public String getTokenContent() {
        return tokenContent;
    }

    public void setTokenContent(String tokenContent) {
        this.tokenContent = tokenContent;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "WrongMessage{" +
                "tokenContent='" + tokenContent + '\'' +
                ", errorCode=" + errorCode +
                ", stage='" + stage + '\'' +
                '}';
    }
}

