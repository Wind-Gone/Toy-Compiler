package com.example.compiler.entity.wrong;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName WrongMessage.java
 * @Description TODO
 * @createTime 2021年05月18日 15:14:00
 */
public enum ErrorCode {
    INT_GREATER_LIMIT("1001", "Intnumber数值最大不超过2^31"),
    NOT_MATCH("1002", "输入的程序存在无法识别的程序模块"),
    EXPONENT_GREATER_LIMIT("1003", "Exponent类型值数字位不能超过128"),
    REALNUMBER_FORMAT_ERROR("1004", "不是正确的Realnumber格式"),


    MISS_SEMICOLON("2001", "程序段可能遗漏分号"),
    MISS_START_OPENCURLYBRACES("2002", "程序缺失开始的左花括号"),
    MISS_END_CLOSECURLYBRACES("2003", "程序缺失结束的右花括号"),
    EXTRA_VARIABLE_USE("2004", "出现了额外的标识符，请确认此处是否应该存在该变量"),
    MISS_OR_EXTRA_OPENBRACE("2005", "请查看程序是否缺失了左括号或出现了多余的右括号"),
    MISS_OR_EXTRA_CLOSEBRACE("2006", "请查看程序是否缺失了右括号或出现了多余的左括号"),
    EXTRA_SEMICOLON("2007", "程序段可能多了额外的分号"),
    WRONG_TYPE_COMPARE("2008", "非法的操作符比较"),
    NO_MATCH_IFELSE_WRONG("2009", "if-else对不匹配"),
    NOT_REASONABLE_SYMBOL("2010", "不合理的操作符，请确认此处是否存在不合理的输入"),
    EXTRA_EQUAL("2011", "不合理的赋值符号出现，请确认是否多加了‘=’"),
    MISS_OR_EXTRA_OPENCURLYBRACE("2012", "请查看程序是否缺失了左花括号或出现了多余的右花括号"),
    MISS_OR_EXTRA_CLOSECURLYBRACE("2013", "请查看程序是否缺失了左花括号或出现了多余的右花括号"),
    WRONG_GRAMMER_PARSER("2014", "非法的语法匹配"),

    NO_EXIST_VARIABLE("3001", "该变量未定义，请检查是否存在未定义先使用的情况"),
    DIFFERENT_TYPES_WARNINGS1("3002", "不同的数据类型不能赋值"),
    DIFFERENT_TYPES_WARNINGS2("3003", "不同的数据类型不能进行比较");


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
