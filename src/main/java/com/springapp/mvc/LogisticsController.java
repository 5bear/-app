package com.springapp.mvc;

import com.alibaba.fastjson.JSON;
import com.springapp.classes.ReturnCode;
import com.springapp.entity.Account;
import com.springapp.entity.Logistics;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 11369 on 2016/10/6.
 */
@Controller
@RequestMapping(value = "Logistics")
public class LogisticsController extends BaseController{
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    /**
     *录入物流信息
     * @param logistics 箱码 经销商 扫码时间: lCode aid qrTime yyyy_MM_dd_HH_mm_ss
     * @param uid 用户id
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public JSON AddGoods(Logistics logistics, Long uid/* String token, String qrTime*/){
        ReturnCode returnCode=new ReturnCode();
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
        }else {
            if(logistics.getAid()==null|| logistics.getlCode()==null||logistics.getlCode().equals("")||logistics.getQrTime().equals("")|| logistics.getQrTime()==null){
                returnCode.setFail("参数不能为空");
                return (JSON)JSON.toJSON(returnCode);
            }
            try {
                Date parseDate=sdf.parse(logistics.getQrTime());
                logistics.setCreateTime(parseDate.getTime());
            } catch (ParseException e) {
                returnCode.setFail("扫码日期格式错误");
                return (JSON)JSON.toJSON(returnCode);
            }
            Logistics tmp = logisticsDao.getByLcodeAndAid(logistics.getlCode(), logistics.getAid());
            //如果存在，更新
            if(tmp!=null){
                tmp.setUid(uid);
                tmp.setQrTime(logistics.getQrTime());
                tmp.setCreateTime(logistics.getCreateTime());
                tmp.setOperationType(logistics.getOperationType());
                tmp.setRemark2(logistics.getRemark2());
                tmp.setRemark3(logistics.getRemark3());
                logisticsDao.update(tmp);
                returnCode.setSuccess("更新成功");
            }else {
                logistics.setUid(account.getId());
                if (logisticsDao.addLogistics(logistics) == null) {
                    returnCode.setFail("添加失败");
                } else {
                    returnCode.setSuccess("添加成功");
                }
            }
        }
        return (JSON)JSON.toJSON(returnCode);
    }

    /**
     * 物流信息分页列表
     * @param pn 页数
     * @param length 长度
     * @param uid 用户id
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public JSON GoodsPageList(Integer pn,Integer length,/*String token*/Long uid){
        ReturnCode returnCode=new ReturnCode();
        if(pn==null||length==null||uid==null){
            returnCode.setFail("参数不能为空");
            return (JSON)JSON.toJSON(returnCode);
        }
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
        }else{
            List<Logistics> agentList= logisticsDao.list(account.getId());
            if(agentList==null)
                agentList=new ArrayList<Logistics>();
            int totalPage=0;
            List<Logistics> pageList;
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
                    pageList = logisticsDao.LogisticsPageList(account.getId(),totalPage, length);
                } else {
                    pageList = logisticsDao.LogisticsPageList(account.getId(),pn, length);
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
