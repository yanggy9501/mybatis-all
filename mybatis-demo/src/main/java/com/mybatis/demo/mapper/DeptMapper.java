package com.mybatis.demo.mapper;

import com.mybatis.demo.entity.Dept;

import java.util.List;

// MapperProxy
public interface DeptMapper {

    Integer insertDept(Dept dept);

    List<Dept> query1(Dept dept);
}
