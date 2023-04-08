package com.mybatis.demo.entity;

/**
 * @author yanggy
 */
public class Dept {

    private String deptName;

    public Dept(String deptName) {
        this.deptName = deptName;
    }

    public Dept() {
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "Dept{" +
            "deptName='" + deptName + '\'' +
            '}';
    }
}
