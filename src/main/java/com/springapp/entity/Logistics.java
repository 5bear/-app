package com.springapp.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Timestamp;

/**
 * Created by 11369 on 2016/10/6.
 * 物流信息 箱码和经销商
 * 每天导出一次excel 邮件 清空
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Logistics {
    /*
    箱码 经销商 时间戳 用户对应的id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long uid;//用户id
    private String lCode;// 箱码
    private Long aid;//经销商aid
    private String qrTime;//扫码时间 yyyy-MM-dd HH:mm:ss
    private Long createTime;
    private String operationType;//DUO BOX WITHDRAW 默认DUO
    /*操作类型 按垛发货，散箱发货、撤回
      默认按垛发货，跟以前处理一样
      标记散箱发货的，导出的时候箱码一样的也都要导出
      导出的Excel新增一列操作类型散箱发货和整垛发货，导出的表格里操作类型列空白不填,撤回的标记为撤回
    */
    private String remark2;//备选字段2
    private String remark3;//备选字段3

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

    public String getlCode() {
        return lCode;
    }

    public void setlCode(String lCode) {
        this.lCode = lCode;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getQrTime() {
        return qrTime;
    }

    public void setQrTime(String qrTime) {
        this.qrTime = qrTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }
}
