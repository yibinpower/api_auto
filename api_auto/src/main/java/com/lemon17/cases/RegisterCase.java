package com.lemon17.cases;


import com.lemon17.constants.Constants;
import com.lemon17.pojo.CaseInfo;
import com.lemon17.utils.ExcelUtils;
import com.lemon17.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: YiBin
 * @Description: 注册 测试类
 * @Date: Created in 下午 03:49 20/05/15
 * @Modified By:
 */
public class RegisterCase extends BaseCase {
    //把读取excel的sheet定义为成员变量
    private int sheetIndex = 0;

    //执行用例之前会自动自行父类中的init方法，用例执行完之后会自动执行finish方法

    // 用例执行的数据源来源于dataProvider = "datas"；datas这个二维数组长度有多大就执行多少次迭代；参数就是数组中的元素
    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) throws IOException {
        replaceParams(caseInfo);//参数化方法替换变量
        String type = caseInfo.getType();
        String contentType = caseInfo.getContentType();
        String url = caseInfo.getUrl();
        String params = caseInfo.getParams();

        //调用父类中获取授权请求头方法得到请求头
        Map<String, String> headers = getAuthenticationHeader();

        //获取响应
        HttpResponse response = HttpUtils.call(type, contentType, url, params, Constants.HEADERS);
        //获取响应体
        String body = HttpUtils.getResponseBody(response);
        System.out.println("响应：" + body);
        System.out.println("============================================================");

//      断言响应结果
        //从实体类中获取预期数据
        String expectedData = caseInfo.getExpectedData();
        //获取断言结果
        Boolean assertFlag = assertResponse(body, expectedData);
        System.out.println("断言结果：" + assertFlag);
    }

    // 数据提供者提供一个二维数组：二维数组长度是excel行数，也为用例执行次数；其内部的一维数组是一个CaseInfo对象，是一个整体
    @DataProvider(name = "datas")
    public Object[][] datas(){
        return ExcelUtils.read(sheetIndex,1, CaseInfo.class);
    }
}
