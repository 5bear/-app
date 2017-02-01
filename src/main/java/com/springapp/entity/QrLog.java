package com.springapp.entity;

/**
 * Created by 11369 on 2017/2/1.
 */
public class QrLog {
    private Long id;
    private String code;//二维码字符串
    private Long timestamp;//扫码时间戳

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
