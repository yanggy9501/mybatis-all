package com.mybatis.demo.mapper;

import com.mybatis.demo.entity.User;

// MapperProxy
public interface UserMapper {

    User selectById(Integer id);

//    void updateForName(String id,String username);
}
