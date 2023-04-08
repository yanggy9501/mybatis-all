package com.mybatis.demo.tools.typehandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.LongTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;
import org.junit.Test;

import java.sql.*;

/**
 * 测试 mybatis 的类型转换器
 * 涉及模式：策略模式
 *
 * @author yanggy
 */
@Slf4j
public class TestTypeHandler {

    @Test
    public void testUseTypeHandler() throws SQLException {
        Connection conn=null;
        PreparedStatement pstmt=null;
        try {
            // 1.加载驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 2.创建连接
            conn= DriverManager.   // SPI
                getConnection("jdbc:mysql://192.168.134.128:3306/db_test", "root", "123456");

            // 开启事务
            conn.setAutoCommit(false);

            // SQL语句  参数#{}  ${}  <if>
            String sql="  select id, username, create_time from user where id=?;";

            // 获得sql执行者  ：
            // 1. 执行预处理
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,2011);

            // 执行查询
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            //ResultSet rs= pstmt.executeQuery();

            // 获取一行结果
            if (!rs.next()) {
                return;
            }

            StringTypeHandler stringTypeHandler = new StringTypeHandler();
            String username = stringTypeHandler.getResult(rs, "username");
            log.info("username -> {}", username);

            LongTypeHandler longTypeHandler = new LongTypeHandler();
            Long id = longTypeHandler.getResult(rs, "id");
            log.info("id -> {}", id);

            // 提交事务
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚事务
            assert conn != null;
            conn.rollback();
        }
        finally{
            // 关闭资源
            try {
                if(conn!=null){
                    conn.close();
                }
                if(pstmt!=null){
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
