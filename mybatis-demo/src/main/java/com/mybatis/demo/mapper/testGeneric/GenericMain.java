package com.mybatis.demo.mapper.testGeneric;

import com.mybatis.demo.entity.User;

import java.util.List;

/**
 * Java中泛型和具体的 class 类型是不通的，泛型只存在于编译器，编译之后就会被擦除。
 *
 *  User user =  session.selectOne("com.mybatis.demo.mapper.UserMapper.selectById", 2011);
 *  User 类型可以换成其他类型的，Dept等等， 但是有类型转换错误的风险。
 */
public class GenericMain {
    public static void main(String[] args) {
        TestInterface testInterface = new TestInterface();
        // 方式1：
        List<User> list = testInterface.listGeneric();
        System.out.println(list.size());

        // 方式2：
        User generic = testInterface.getGeneric();
        System.out.println(generic);

        testInterface.save(new Object());
    }
}
