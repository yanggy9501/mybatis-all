package com.mybatis.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*

CREATE TABLE `user` (
	`id` INT ( 11 ) NOT NULL AUTO_INCREMENT,
	`username` VARCHAR ( 32 ) DEFAULT NULL,
	`roles` VARCHAR ( 255 ) DEFAULT NULL,
	`create_time` DATETIME DEFAULT NULL,
	PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 2008 DEFAULT CHARSET = utf8;

 */

public class User implements Serializable{

    private Long id ;
    public String username ;
    private Date createTime;
    private List<String> roles;
    private Dept dept;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", createTime=" + createTime +
            ", roles=" + roles +
            ", dept=" + dept +
            '}';
    }
}
