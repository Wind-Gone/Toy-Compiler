package com.example.compiler.entity.wrong;

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
    REALNUMBER_FORMAT_ERROR("1004", "不是正确的Realnumber格式"),


    MISS_SEMICOLON("2001", "程序段可能遗漏分号"),
    MISS_START_OPENCURLYBRACES("2002", "程序缺失开始的左花括号"),
    MISS_END_CLOSECURLYBRACES("2003", "程序缺失结束的右花括号"),
    EXTRA_VARIABLE_USE("2004", "出现了额外的标识符，请查看是否滥用了变量"),
    MISS_OR_EXTRA_OPENBRACE("2005", "请查看程序是否缺失了左括号或出现了多余的右括号"),
    MISS_OR_EXTRA_CLOSEBRACE("2006", "请查看程序是否缺失了右括号或出现了多余的左括号"),
    EXTRA_SEMICOLON("2007", "程序段可能多了额外的分号"),
    WRONG_TYPE_COMPARE("2008", "非法的操作符比较"),
    WRONG_GRAMMER_PARSER("2009", "非法的语法匹配");


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
