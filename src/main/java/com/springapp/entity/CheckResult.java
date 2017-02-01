package com.springapp.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by 11369 on 2016/10/26.
 * 检测结果
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CheckResult {
    private Long id;
    private Long uid; //用户id
    private String pCde;//垛码
    private Integer result;//0 检测合格 1 表示不合格
    private Long recordTime;//记录时间戳

    @Id
    @GeneratedValue
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
    //垛码为主键 覆盖
    @Id
    public String getpCde() {
        return pCde;
    }

    public void setpCde(String pCde) {
        this.pCde = pCde;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Long recordTime) {
        this.recordTime = recordTime;
    }
}
