package com.mybatis.demo;

import com.mybatis.demo.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Date;

/**
 * @author yanggy
 */
public class TestMybatis {

    @Test
    public void test01() throws IOException {
        String resource = "mybatis-config.xml";
        // 将XML配置文件构建为Configuration配置类
        Reader reader = Resources.getResourceAsReader(resource);
        // 通过加载配置文件流构建一个SqlSessionFactory   解析xml文件  1
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // 数据源 执行器  DefaultSqlSession 2 + 自动提交事务
        SqlSession session = sqlSessionFactory.openSession(true);
        User user = new User();
//        user.setId(100L);
        user.setUsername("xxx");
        user.setRoles(Arrays.asList("dp", "ll", "tyw"));
        user.setCreateTime(new Date());
        session.insert("com.mybatis.demo.mapper.UserMapper.insert", user);
//        session.commit();

        Object rs = session.selectOne("com.mybatis.demo.mapper.UserMapper.selectById", 5);
        System.out.println(rs);
        session.close();
    }

    @Test
    public void testQuery1() throws IOException {
        String resource = "mybatis-config.xml";
        // 将XML配置文件构建为Configuration配置类
        Reader reader = Resources.getResourceAsReader(resource);
        // 通过加载配置文件流构建一个SqlSessionFactory   解析xml文件  1
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // 数据源 执行器 DefaultSqlSession 2 + 自动提交事务
        SqlSession session = sqlSessionFactory.openSession(true);

        Object rs = session.selectOne("com.mybatis.demo.mapper.UserMapper.selectById", 1);
        System.out.println(rs);
        session.close();
    }

    @Test
    public void testSelectTypeHandler1() throws IOException {
        String resource = "mybatis-config.xml";
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // 将XML配置文件构建为Configuration配置类
        Reader reader = Resources.getResourceAsReader(resource);
        // 通过加载配置文件流构建一个SqlSessionFactory   解析xml文件  1
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // 数据源 执行器  DefaultSqlSession 2 + 自动提交事务
        SqlSession session = sqlSessionFactory.openSession(true);

        Object rs = session.selectOne("com.mybatis.demo.mapper.UserMapper.selectById", 5);
        System.out.println(rs);
    }

}
