package com.mybatis.demo;

import
    com.mybatis.demo.entity.Dept;
import com.mybatis.demo.entity.User;
import com.mybatis.demo.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class App {
    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        // 将XML配置文件构建为Configuration配置类
        Reader reader = Resources.getResourceAsReader(resource);
        // 通过加载配置文件流构建一个SqlSessionFactory 解析xml文件  1
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        // 数据源 执行器  DefaultSqlSession2
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 执行查询 底层执行jdbc 3
            User user1 =  session.selectOne("com.mybatis.demo.mapper.UserMapper.selectById", 1);

            User user2 =  session.selectOne("com.mybatis.demo.mapper.UserMapper.selectById", 1);

            // 创建动态代理
            UserMapper mapper = session.getMapper(UserMapper.class);
            System.out.println(mapper.getClass());
            User user = mapper.selectById(1);
            System.out.println(user.getUsername());
            session.commit();
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }



}
