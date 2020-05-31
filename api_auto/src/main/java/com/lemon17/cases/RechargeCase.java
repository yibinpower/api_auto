package com.lemon17.cases;


import com.alibaba.fastjson.JSONPath;
import com.lemon17.constants.Constants;
import com.lemon17.pojo.CaseInfo;
import com.lemon17.pojo.WriteBackData;
import com.lemon17.utils.AuthenticationUtils;
import com.lemon17.utils.ExcelUtils;
import com.lemon17.utils.HttpUtils;
import com.lemon17.utils.SQLUtils;
import com.sun.org.glassfish.external.probe.provider.annotations.ProbeListener;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: YiBin
 * @Description: 充值 测试类
 * @Date: Created in 下午 03:49 20/05/15
 * @Modified By:
 */
public class RechargeCase extends BaseCase {
    //把读取excel的sheet定义为成员变量
    private int sheetIndex = 2;

    //执行用例之前会自动自行父类中的init方法，用例执行完之后会自动执行finish方法

    // 用例执行的数据源来源于dataProvider = "datas"；datas这个二维数组长度有多大就执行多少次迭代；参数就是数组中的元素
    @Step()
    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) throws IOException {
        replaceParams(caseInfo);//参数化方法替换变量
        String type = caseInfo.getType();
        String contentType = caseInfo.getContentType();
        String url = caseInfo.getUrl();
        String params = caseInfo.getParams();
        String sql = caseInfo.getSQL();

        //数据库前置查询
        Object beforeSqlResult = null;
        if (StringUtils.isNotBlank(sql)){
            beforeSqlResult = SQLUtils.getSingleResult(sql);
        }

        //调用父类中获取授权请求头方法得到请求头
        Map<String, String> headers = getAuthenticationHeader();

        //获取响应
        HttpResponse response = HttpUtils.call(type, contentType, url, params, headers);
        //获取响应体
        String body = HttpUtils.getResponseBody(response);
        System.out.println("响应：" + body);

//      断言响应结果
        //从实体类中获取预期数据
        String expectedData = caseInfo.getExpectedData();
        //获取断言结果
        Boolean assertFlag = assertResponse(body, expectedData);
        System.out.println("断言结果：" + assertFlag);

        //添加回写内容
        WriteBackData wbd = new WriteBackData(2, caseInfo.getCaseId(), Constants.RESPONSE_CELL_NUM, body);
        ExcelUtils.wbdList.add(wbd);//把回写对象添加到集合中，测试套件执行完后批量回写

        //数据库后置查询
        Object afterSqlResult = null;
        if (StringUtils.isNotBlank(sql)){
            afterSqlResult = SQLUtils.getSingleResult(sql);
        }

        //数据库断言
        boolean sqlAssertFlag = true;
        if (StringUtils.isNotBlank(sql)){
            sqlAssertFlag = sqlAssert(beforeSqlResult,afterSqlResult,params);
            System.out.println("数据库断言结果：" + sqlAssertFlag);
        }

        //断言回写
        String assertContent = assertFlag && sqlAssertFlag ? "pass" : "failed";
        WriteBackData wbd2 = new WriteBackData(2, caseInfo.getCaseId(), Constants.ASSERT_CELL_NUM, assertContent);
        ExcelUtils.wbdList.add(wbd2);

        //报表断言
        Assert.assertEquals(assertFlag && sqlAssertFlag, true);

        System.out.println("============================================================");
    }


    // 数据提供者提供一个二维数组：二维数组长度是excel行数，也为用例执行次数；其内部的一维数组是一个CaseInfo对象，是一个整体
    @DataProvider(name = "datas")
    public Object[][] datas(){
        return ExcelUtils.read(sheetIndex,1, CaseInfo.class);
    }


    /**
     * 充值用例的数据库断言方法
     * @param beforeSqlResult
     * @param afterSqlResult
     * @param params
     * @return
     */
    public boolean sqlAssert(Object beforeSqlResult, Object afterSqlResult, String params){
        //预期结果
        Object expectedSqlResult = JSONPath.read(params, "$.amount");
        //前置查询得到的值
        BigDecimal beforeSqlValue = (BigDecimal) beforeSqlResult;
        //后置查询得到的值
        BigDecimal afterSqlValue = (BigDecimal) afterSqlResult;
        //预期值
        BigDecimal expectedSqlValue = new BigDecimal(expectedSqlResult.toString());
        //判断，当 expectedSqlValue=afterSqlValue-beforeSqlValue时，断言成功
        if (afterSqlValue.subtract(beforeSqlValue).compareTo(expectedSqlValue)==0){
            return true;
        }else {
            return false;
        }
    }

}
