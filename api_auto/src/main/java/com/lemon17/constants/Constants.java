package com.lemon17.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YiBin
 * @Description: 常量类
 * @Date: Created in 上午 11:03 20/05/16
 * @Modified By:
 */
public class Constants {
    //默认请求头
    public static final Map<String,String> HEADERS = new HashMap<>();
    //excel路径
    public static final String EXCEL_PATH = "lib/cases_v6.xlsx";
    //响应回写列
    public static final int RESPONSE_CELL_NUM = 7;
    //断言回写列
    public static final int ASSERT_CELL_NUM = 10;
}
