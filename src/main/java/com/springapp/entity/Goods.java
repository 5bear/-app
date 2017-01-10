package com.springapp.entity;

import javax.persistence.*;

/**
 * Created by 11369 on 2016/12/27.
 * 商品信息 箱码 串码 salt 产品类型
 */
@Entity
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String lCode; //箱码
    @Column(unique = true)
    private String gCode; //串码 产品缩写+八位随机数
    private String salt;  //MD5用到salt
    private String gType; //产品类型缩写
    private String gTypeInfo; //产品类型详细信息

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getlCode() {
        return lCode;
    }

    public void setlCode(String lCode) {
        this.lCode = lCode;
    }

    public String getgCode() {
        return gCode;
    }

    public void setgCode(String gCode) {
        this.gCode = gCode;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }

    public String getgTypeInfo() {
        return gTypeInfo;
    }

    public void setgTypeInfo(String gTypeInfo) {
        this.gTypeInfo = gTypeInfo;
    }
}
