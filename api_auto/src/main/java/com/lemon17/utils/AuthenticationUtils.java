package com.lemon17.utils;

import com.alibaba.fastjson.JSONPath;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YiBin
 * @Description: 鉴权工具类
 * @Date: Created in 下午 02:03 20/05/21
 * @Modified By:
 */
public class AuthenticationUtils {
    public static Map<String,Object> ENV = new HashMap<>();


    /**
     * 使用JSONPath表达式获取值存储到环境变量中
     * @param json
     * @param expression
     * @param key
     */
    public static void getValue2ENV(String json, String expression, String key){
        Object obj = JSONPath.read(json, expression);
        if (obj != null){
            ENV.put(key,obj);
        }
    }

}
