package com.springapp.classes;

/**
 * Created by 11369 on 2016/9/1.
 * 请求返回内容
 */
public class ReturnCode {
    private String status;//成功或者失败
    private String error;//失败内容
    private Object result;//成功数据

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    public void setSuccess(Object result){
        this.setStatus("success");
        this.setResult(result==null?"null":result);
        this.error="";
    }
    public void setFail(String error){
        this.setStatus("fail");
        this.setError(error);
        this.result="null";
    }
}
