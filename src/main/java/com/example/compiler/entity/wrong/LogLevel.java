package com.example.compiler.entity.wrong;

/**
 * @author Hu Zirui
 * @version 1.0.0
 * @ClassName LogLevel.java
 * @Description TODO
 * @createTime 2021年06月16日 13:17:00
 */

public class LogLevel {
    int row;
    int startCol;
    int endCol;

    public LogLevel(int row, int startCol, int endCol) {
        this.row = row;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getEndCol() {
        return endCol;
    }

    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }
}