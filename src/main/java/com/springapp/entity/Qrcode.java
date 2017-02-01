package com.springapp.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by 11369 on 2016/10/26.
 * 失败的二维码
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Qrcode {
    private Long id;
    private Long uid;//用户id
    private String path;//存储位置
    private String code;//垛码
    private Long qrTime;//扫码时间
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getQrTime() {
        return qrTime;
    }

    public void setQrTime(Long qrTime) {
        this.qrTime = qrTime;
    }
}
