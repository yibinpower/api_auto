package com.lemon17.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @Author: YiBin
 * @Description: httpclient的get、post、patch请求封装，获取响应封装，json格式化方法封装，以及请求方法调度方法call封装
 * @Date: Created in 上午 09:50 20/05/15
 * @Modified By:
 */
public class HttpUtils {
    public static Logger logger = Logger.getLogger(HttpUtils.class);

    public static void main(String[] args) throws IOException {
        //测试call方法
        //用户信息接口验证get
//        String body = call("get", "json", "http://api.lemonban.com/futureloan/member/8882574/info", "");
//        System.out.println(body);
//        System.out.println("=========================================================================");
//        //注册接口验证post
//        String body2 = call("post", "json", "http://api.lemonban.com/futureloan/member/register", "{\"mobile_phone\": \"13012344321\",\"pwd\": \"12345678\"}");
//        System.out.println(body2);


        //测试自定义的get方法
//        String url_info = "http://api.lemonban.com/futureloan/member/8882574/info";
//        try {
//            String prettyEntity = get(url_info);
//            System.out.println(prettyEntity);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //测试自定义的post方法
//        String url_register = "http://api.lemonban.com/futureloan/member/register";
//        String param = "{\"mobile_phone\": \"13012344321\",\"pwd\": \"12345678\"}";
//        try {
//            String prettyResponse = post(url_register, param);
//            System.out.println(prettyResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    /**
     * 请求的调度方法，根据请求类型（post、get、patch）和参数类型（json、form）来选择具体的请求方法
     *
     * @param type
     * @param contentType
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static HttpResponse call(String type, String contentType, String url, String param, Map<String, String> headers) {
        HttpResponse response = null;
        try {
            if ("post".equalsIgnoreCase(type)) {
                if ("json".equals(contentType)) {
                    response = post(url, param, headers);
                } else if ("form".equals(contentType)) {
                    response = formPost(url, param, headers);
                }
            } else if ("get".equals(type)) {
                response = get(url,headers);
            } else if ("patch".equals(type)) {
                response = patch(url, param, headers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * http的get请求方法封装
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpResponse get(String url,Map<String, String> headers) throws IOException {
        HttpGet get = new HttpGet(url);
//        get.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
        setHeaders(headers,get);

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(get);

        getRequest(get);
//        return getResponseBody(response);
        return response;
    }


    /**
     * http的post请求方法封装
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static HttpResponse post(String url, String param, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        setHeaders(headers, httpPost);

        httpPost.setEntity(new StringEntity(param, "utf-8"));
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpPost);

        getRequest(httpPost);
        logger.info("请求体：" + EntityUtils.toString(httpPost.getEntity()));
//        return getResponseBody(response);
        return response;
    }


    /**
     * http的post请求方法封装
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static HttpResponse patch(String url, String param,Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
//        httpPost.setHeader("Content-Type", "application/json");
        setHeaders(headers,httpPost);

        httpPost.setEntity(new StringEntity(param, "utf-8"));
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpPost);

        getRequest(httpPost);
        logger.info("请求体：" + EntityUtils.toString(httpPost.getEntity()));
//        return getResponseBody(response);
        return response;
    }


    /**
     * http的form表单post请求方法封装
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static HttpResponse formPost(String url, String param, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("X-Lemonban-Media-Type","lemonban.v1");
//        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
        setHeaders(headers,httpPost);
        String form = json2KeyValue(param);
        httpPost.setEntity(new StringEntity(form, "utf-8"));
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpPost);

        getRequest(httpPost);
        logger.info("请求体：" + EntityUtils.toString(httpPost.getEntity()));
//        return getResponseBody(response);
        return response;
    }



    /**
     * json字符串转为key=value的form表单的字符串
     *
     * @param json
     * @return
     */
    public static String json2KeyValue(String json) {
        if (StringUtils.isEmpty(json)) {
            return "";
        }

        String result = "";
        Map map = JSONObject.parseObject(json, Map.class);
        Set<String> set = map.keySet();
        for (String key : set) {
            result += key + "=" + map.get(key) + "&";
        }
        result = result.substring(0, result.length() - 1);

        return result;
    }


    /**
     * 封装响应内容的方法
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String getResponseBody(HttpResponse response) throws IOException {
        //获取响应头
        Header[] allHeaders = response.getAllHeaders();
        //获取响应体
        HttpEntity entity = response.getEntity();
        //获取状态码
        response.getStatusLine().getStatusCode();

//        System.out.println(Arrays.toString(allHeaders));
        String body = EntityUtils.toString(entity);

        //格式化json字符串
        body = jsonFormat(body);
//        System.out.println(body);

        return body;
    }


    /**
     * 获取请求头、体
     *
     * @param httpRequest
     */
    public static void getRequest(HttpRequest httpRequest) {
        //请求头
        String headers = Arrays.toString(httpRequest.getAllHeaders());
        //请求体
        String requests = httpRequest.getRequestLine().toString();
        logger.info("请求：" + headers + "\n" + requests );

    }


    /**
     * 设置请求头方法抽取
     *
     * @param headers
     * @param httpRequest
     */
    public static void setHeaders(Map<String, String> headers, HttpRequest httpRequest) {
        Set<String> set = headers.keySet();
        for (String key : set) {
            String value = headers.get(key);
            //设置请求头
            httpRequest.setHeader(key, value);
        }
    }


    /**
     * json字符串格式化
     *
     * @param json_str
     * @return
     */
    public static String jsonFormat(String json_str) {
        JSONObject object = JSONObject.parseObject(json_str);
        String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return pretty;
    }

}
