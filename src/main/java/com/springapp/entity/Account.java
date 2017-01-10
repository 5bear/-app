package com.springapp.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by 11369 on 2016/10/6.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Account {
    private Long id;
    private String username;
    private String password;
    private String token; //暂时不使用
    private String dcName;//所属仓库
    //不用单点登录
    //添加一个字段 仓库名称 dcName
   /* private Long recordTime;//记录时间戳
    private Float successRate;//识别成功率*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column
    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }
}
