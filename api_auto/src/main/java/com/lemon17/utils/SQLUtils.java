package com.lemon17.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: YiBin
 * @Description: SQL操作的工具类
 * @Date: Created in 下午 03:04 20/05/26
 * @Modified By:
 */
public class SQLUtils {
    public static void main(String[] args) {
        String sql = "select leave_amount from member a where id = 100051295";
        System.out.println(getSingleResult(sql));
    }


    /**
     * 执行查询sql，返回单个结果集
     * @param sql
     * @return
     */
    public static Object getSingleResult(String sql){
        try {
            //1.创建runner对象
            QueryRunner runner = new QueryRunner();
            //2.获取数据库连接
            Connection connection = JDBCUtils.getConnection();
            //3.执行查询
            Object result = runner.query(connection, sql, new ScalarHandler<>());
            //4.关闭连接
            JDBCUtils.close(connection);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
