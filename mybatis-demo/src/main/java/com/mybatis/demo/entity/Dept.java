package com.mybatis.demo.entity;

/**
 * @author yanggy
 */
public class Dept {
    Long id;

    private String deptName;

    public Dept() {

    }

    public Dept(String deptName) {
        this.deptName = deptName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
            "id=" + id +
            ", deptName='" + deptName + '\'' +
            '}';
    }
}
