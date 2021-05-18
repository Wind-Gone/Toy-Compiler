package com.example.compiler.entity;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName WrongMessage.java
 * @Description TODO
 * @createTime 2021年05月18日 15:14:00
 */
public enum ErrorCode {
    // TODO ADD MORE POSSIBLE ERRORCODE
    INT_GREATER_LIMIT("1001", "Intnumber数值最大不超过2^31"),
    NOT_MATCH("1002", "输入的程序存在无法识别的程序模块"),
    EXPONENT_GREATER_LIMIT("1003", "Exponent类型值数字位不能超过128"),
    REALNUMBER_FORMAT_ERROR("1004", "不是正确的Realnumber格式");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorCode get(String str) {
        for (ErrorCode e : values()) {
            if (e.getCode().equals(str))
                return e;
        }
        return null;
    }
}
