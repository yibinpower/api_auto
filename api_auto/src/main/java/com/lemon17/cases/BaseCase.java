package com.lemon17.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.lemon17.constants.Constants;
import com.lemon17.pojo.CaseInfo;
import com.lemon17.utils.AuthenticationUtils;
import com.lemon17.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 所有用例的父类
 */
public class BaseCase {
    public static Logger logger = Logger.getLogger(BaseCase.class);

    @BeforeSuite
    public void init(){
        Constants.HEADERS.put("X-Lemonban-Media-Type", "lemonban.v2");
        Constants.HEADERS.put("Content-Type", "application/json");
//        Constants.HEADERS.put("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjg4Mzg5MzUsImV4cCI6MTU5MDA1MTQ1MH0.MDuHjgIdJCeMW2aZug9szVlZmbrBYEF1gDp6uGiajI4v4Zxfbd1hS_wJeeh7_E2AL_mA1ZXdPVwQNitRs39SyA");

    //加载参数化配置文件
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("lib/params.properties");
            prop.load(fis);
            //把加载的配置文件中的参数添加到环境变量中
            AuthenticationUtils.ENV.putAll((Map)prop);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    @AfterSuite
    public void finish(){
        ExcelUtils.backWrite();
    }



    /**
     * 获取授权请求头方法：从环境变量中获取token，把其添加到请求头中
     * @return
     */
    public Map<String, String> getAuthenticationHeader() {
        Map<String,String> headers = new HashMap<>();
        //获取登录的token
        Object token = AuthenticationUtils.ENV.get("token");
//        System.out.println("token的值："+token);
        //把token加到请求头中
        headers.putAll(Constants.HEADERS);
        headers.put("Authorization", "Bearer " + token);
//        System.out.println("打印headers：");
//        Set<String> set = headers.keySet();
//        for (String key:set){
//            System.out.println(key + ":" + headers.get(key));
//        }
        return headers;
    }


    /**
     * 断言响应结果
     * @param body            响应体
     * @param expectedData    预期数据
     * @return                返回断言结果 true/false
     */
    public Boolean assertResponse(String body, String expectedData) {
        //定义一个断言标识
        Boolean assertFlag = true;
        //转Map
        Map<String, Object> expectedMap = JSONObject.parseObject(expectedData, Map.class);
        //变量Map {"$.code":0,"$.msg":"OK"}
        Set<String> keySet = expectedMap.keySet();
        for (String jsonPathKey : keySet){
            //预期的值 0或"OK"
            Object expectedValue = expectedMap.get(jsonPathKey);
            //使用jsonPathKey去body中找到的实际的值
            Object actualValue = JSONPath.read(body, jsonPathKey);
            //实际值和预期值进行比较
            if (actualValue != null){
                if(!actualValue.equals(expectedValue)){
                    assertFlag = false;
                    break;
                }
            }else {
                System.out.println("实际值为null！");
            }
        }
        return assertFlag;
    }



    /**
     * 参数化替换方法
     * @param caseInfo
     */
    public void replaceParams(CaseInfo caseInfo){
        String params = caseInfo.getParams();
        String expectedData = caseInfo.getExpectedData();
        String sql = caseInfo.getSQL();

        Set<String> keySet = AuthenticationUtils.ENV.keySet();
        for (String oldKey : keySet){
            String newValue = AuthenticationUtils.ENV.get(oldKey).toString();
            if (StringUtils.isNotBlank(params)) {
                params = params.replace(oldKey, newValue);
                caseInfo.setParams(params);
            }
            if (StringUtils.isNotBlank(expectedData)){
                expectedData = expectedData.replace(oldKey, newValue);
                caseInfo.setExpectedData(expectedData);
            }if (StringUtils.isNotBlank(sql)){
                sql = sql.replace(oldKey, newValue);
                caseInfo.setSQL(sql);
            }
        }
    }

}
