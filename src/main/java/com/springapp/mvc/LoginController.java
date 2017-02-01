package com.springapp.mvc;

import com.springapp.classes.ReturnCode;
import com.springapp.entity.Account;
import com.alibaba.fastjson.*;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by 11369 on 2016/10/6.
 */
@Controller
@RequestMapping(value = "/Login")
public class LoginController extends BaseController {
    /**
     *登录检测
     * @param username 用户名
     * @param password 密码
     * @return jsonString 例：{"error":"","result":{"id":1,"password":"jw","token":"FOR9ivMSNW","username":"jw"},"status":"success"}
     */
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    @ResponseBody
    public JSON loginCheck(String username, String password){
        ReturnCode returnCode=new ReturnCode();
        Account account=accountDao.loginCheck(username);
        if(account==null){
            returnCode.setFail("用户不存在");
            return (JSON) JSON.toJSON(returnCode);
        }else{
            if(!account.getPassword().equals(password)){
                returnCode.setFail("密码错误");
                return (JSON)JSON.toJSON(returnCode);
            }
        }
        String token= RandomStringUtils.randomAlphanumeric(10);
        if(token==null) {
            returnCode.setFail("未知的错误");
            return (JSON)JSON.toJSON(returnCode);
        }
        else {
            account.setToken(token);
            accountDao.update(account);
            returnCode.setSuccess(account);
        }
        return (JSON)JSON.toJSON(returnCode);
    }

    /**
     * 注销
     * @param uid 登录用户id
     * @return
     */
    @RequestMapping(value = "/exit",method = RequestMethod.POST)
    @ResponseBody
    public JSON Logout(/*String token*/Long uid){
        ReturnCode returnCode=new ReturnCode();
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请先登录");
        }else{
            account.setToken("");
            accountDao.update(account);
            returnCode.setSuccess("注销成功");
        }
        return (JSON)JSON.toJSON(returnCode);
    }
}
