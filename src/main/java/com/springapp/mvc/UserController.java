package com.springapp.mvc;

import com.springapp.classes.MD5;
import com.springapp.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 11369 on 2017/1/22.
 */
@RequestMapping(value = "/User")
@Controller
public class UserController extends BaseController {
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public ModelAndView add(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/add");
        return modelAndView;
    }

    /**
     * 添加用户 全部必填
     * @param username
     * @param password
     * @param dcName
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(String username, String password, String dcName){
        Account account = accountDao.loginCheck(username);
        if(account != null){
            return "已存在该用户";//已存在该用户
        }
        account = new Account();
        try {
            account.setUsername(username);
            account.setPassword(password);
            account.setDcName(dcName);
            accountDao.save(account);
        }catch (Exception e){
            return "添加失败";
        }
        return "添加成功";
    }
}
