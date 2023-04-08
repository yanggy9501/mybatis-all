package com.mybatis.demo.tools;

import com.mybatis.demo.entity.Dept;
import com.mybatis.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.junit.Test;

import java.util.Date;

/**
 * 测试反射工具
 *
 * @author yanggy
 */
@Slf4j
public class TestMetaObject {

    @Test
    public void testMetaObject() {
        User user = new User();
        user.setId(100L);
        user.setUsername("xxx");
        user.setCreateTime(new Date());
        user.setDept(new Dept("dept"));

        MetaObject metaObject = MetaObject.forObject(user,
            new DefaultObjectFactory(),
            new DefaultObjectWrapperFactory(),
            new DefaultReflectorFactory());

        // 1. 替换属性值
        metaObject.setValue("username", "kato");
        // 复杂属性赋值
        metaObject.setValue("dept.deptName", "ceo");
        log.info(user.toString());

        // 2. 取值
        Object name = metaObject.getValue("username");
        // 复杂对象的取值：sql 解析 col = #{aaa.bbb}
        Object obj = metaObject.getValue("dept.deptName");
        log.info(name.toString());

        // null 了也可以赋值，框架会创建对象：ObjectFactory 的功劳，其会创建对象
        user.setDept(null);
        metaObject.setValue("dept.deptName", "ceo");
        log.info(user.toString());
    }
}
