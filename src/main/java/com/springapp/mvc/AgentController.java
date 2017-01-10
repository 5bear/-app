package com.springapp.mvc;


import com.alibaba.fastjson.JSON;
import com.springapp.classes.ReturnCode;
import com.springapp.entity.Account;
import com.springapp.entity.Agent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 11369 on 2016/10/6.
 */
@Controller
@RequestMapping(value = "Agent")
public class AgentController extends BaseController {
    /**
     *添加经销商
     * @param agent 经销商(不重复)
     * @param token 登录token
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public JSON addAgent(String agent, String token){
        ReturnCode returnCode=new ReturnCode();
        Account account= accountDao.getAccountByToken(token);
        if(account==null){
            returnCode.setFail("请首先登录");
        }else{
            Agent agent1=agentDao.addAgent(account.getId(),agent);
            if(agent1==null){
                returnCode.setFail("重复的经销商");
            }else{
                returnCode.setSuccess("添加成功");
            }
        }
        return (JSON)JSON.toJSON(returnCode);
    }

    /**
     * 更新经销商
     * @param guid 经销商id
     * @param agent 新的经销商
     * @param token 登录token
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public JSON UpdateAgent(String guid,String agent,String token){
        ReturnCode returnCode=new ReturnCode();
        Account account= accountDao.getAccountByToken(token);
        if(account==null){
            returnCode.setFail("请首先登录");
        }else{
            Agent agent1=agentDao.updateAgent(guid, agent);
            if(agent1==null){
                returnCode.setFail("更新失败");
            }else{
                returnCode.setSuccess("更新成功");
            }
        }
        return (JSON)JSON.toJSON(returnCode);
    }

    /**
     * 删除经销商
     * @param guid 经销商id
     * @param token 登录token
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public JSON DeleteAgent(String guid,String token){
        ReturnCode returnCode=new ReturnCode();
        Account account= accountDao.getAccountByToken(token);
        if(account==null){
            returnCode.setFail("请首先登录");
        }else{
            Agent agent1=agentDao.deleteAgent(guid);
            if(agent1==null){
                returnCode.setFail("删除失败");
            }else{
                returnCode.setSuccess("删除成功");
            }
        }
        return (JSON)JSON.toJSON(returnCode);
    }

    /**
     * 分页获取经销商
     * @param pn 当前页数
     * @param length 每页长度
     * @param uid 用户id
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public JSON AgentPageList(Integer pn,Integer length, Long uid){
        ReturnCode returnCode=new ReturnCode();
        if(pn==null||length==null||uid==null){
            returnCode.setFail("参数不能为空");
            return (JSON)JSON.toJSON(returnCode);
        }
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
        }else{
            List<Agent> agentList=agentDao.list(account.getId());
            if(agentList==null)
                agentList=new ArrayList<Agent>();
            int totalPage=0;
            List<Agent> pageList;
            if(pn==0&&length==0){
                pageList=agentList;
                totalPage=1;
            }else {
                if (agentList.size() % length == 0)
                    totalPage = agentList.size() / length;
                else {
                    totalPage = agentList.size() / length + 1;
                }
                if (pn > totalPage) {
                    pageList = agentDao.AgentPageList(account.getId(),totalPage, length);
                } else {
                    pageList = agentDao.AgentPageList(account.getId(),pn, length);
                }
            }
            Map map=new HashMap();
            map.put("totalPage",totalPage);
            map.put("list",pageList);
            returnCode.setSuccess(JSON.toJSON(map));
        }
        return (JSON)JSON.toJSON(returnCode);
    }
}
