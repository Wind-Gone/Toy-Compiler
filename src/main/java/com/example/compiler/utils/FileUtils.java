package com.example.compiler.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName FileUtils.java
 * @Description TODO
 * @createTime 2021年06月09日 11:09:00
 */
public class FileUtils {
    public static String ReadFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
        int length = inputStream.available();
        byte[] bytes = new byte[length];
        int ResultCode = inputStream.read(bytes);
        inputStream.close();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
