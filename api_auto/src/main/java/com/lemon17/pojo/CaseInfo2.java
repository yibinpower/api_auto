package com.lemon17.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @Author: YiBin
 * @Description: 实体类封装，对应着excel用例字段；此实体类对应着excel中的“API”这个sheet
 * @Date: Created in 上午 10:46 20/05/19
 * @Modified By:
 */
public class CaseInfo2 {
    @Excel(name="A")
    private String a;
    @Excel(name="B")
    private String b;

    public CaseInfo2() {
    }

    public CaseInfo2(String a, String b) {
        this.a = a;
        this.b = b;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "CaseInfo2{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                '}';
    }
}
