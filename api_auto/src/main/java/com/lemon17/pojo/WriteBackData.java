package com.lemon17.pojo;

/**
 * @Author: YiBin
 * @Description: 回写的实体类
 * @Date: Created in 下午 05:11 20/05/21
 * @Modified By:
 */
public class WriteBackData {
    //回写excel的sheet下标
    private int sheetIndex;
    //回写的行号
    private int rowNum;
    //回写的“列”
    private int cellNum;
    //回写的内容
    private String content;

    public WriteBackData() {
    }

    public WriteBackData(int sheetIndex, int rowNum, int cellNum, String content) {
        this.sheetIndex = sheetIndex;
        this.rowNum = rowNum;
        this.cellNum = cellNum;
        this.content = content;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WriteBackData{" +
                "sheetIndex=" + sheetIndex +
                ", rowNum=" + rowNum +
                ", cellNum=" + cellNum +
                ", content='" + content + '\'' +
                '}';
    }
}
