package com.springapp.mvc;

import com.alibaba.fastjson.JSON;
import com.springapp.classes.Base64Image;
import com.springapp.classes.ReturnCode;
import com.springapp.entity.Account;
import com.springapp.entity.QrLog;
import com.springapp.entity.Qrcode;
import com.springapp.entity.CheckResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;

/**
 * Created by 11369 on 2016/10/26.
 */
@Controller
@RequestMapping(value = "QrCode")
public class QrcodeController extends BaseController {
    /**
     * 上传扫描失败的二维码  UploadQrCode  post
     * @param session
     * @param uid Long 用户id
     * @param base64Image String 图片base64编码
     * @param pCode 垛码
     * @param qrTime 扫码时间戳
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public JSON UploadQrCode(HttpSession session,/*String token*/Long uid,String base64Image, String pCode, Long qrTime){
        ReturnCode returnCode=new ReturnCode();
        if(uid==null||base64Image==null){
            returnCode.setFail("参数不能为空");
            return (JSON)JSON.toJSON(returnCode);
        }
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
            return (JSON)JSON.toJSON(returnCode);
        }else{
            try {
                String realPath = session.getServletContext().getRealPath("/WEB-INF/pages/qrcode/");
                File mainFile = new File(realPath);
                //文件保存目录URL
                String newFileName ;
                if (!mainFile.exists())
                    mainFile.mkdirs();
                newFileName = new Date().getTime() + "." + "jpg";
                Base64Image.GenerateImage(base64Image, realPath + "/" + newFileName);
                Qrcode qrcode=new Qrcode();
                qrcode.setUid(account.getId());
                qrcode.setPath("qrcode"+newFileName);
                qrcode.setCode(pCode);
                qrcode.setQrTime(qrTime);
                qrcodeDao.save(qrcode);
                returnCode.setSuccess("操作成功");
                return (JSON)JSON.toJSON(returnCode);
            }catch (Exception e){
                returnCode.setFail("操作失败");
                return (JSON)JSON.toJSON(returnCode);
            }
        }
    }

    /**
     * 检测结果
     * @param uid 用户id
     * @param timestamp 时间戳
     * @param status 0 合格 1 不合格
     * @param pCode 垛码
     * @return
     */
    @RequestMapping(value = "/CheckResult",method = RequestMethod.POST)
    @ResponseBody
    public JSON CheckResult(/*String token*/Long uid,Long timestamp, Integer status, String pCode){
        ReturnCode returnCode=new ReturnCode();
        if(uid==null||timestamp==null||status==null){
            returnCode.setFail("参数不能为空");
            return (JSON)JSON.toJSON(returnCode);
        }
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
            return (JSON)JSON.toJSON(returnCode);
        }else{
            try {
                CheckResult checkResult= qrcodeDao.getByPcode(pCode);
                boolean flag = true;
                if(checkResult != null) {
                    flag = false;//如果存在 更新
                }else{
                    checkResult = new CheckResult();
                }
                checkResult.setUid(account.getId());
                checkResult.setRecordTime(timestamp);
                checkResult.setResult(status);
                checkResult.setpCode(pCode);
                if(flag)
                    baseDao.save(checkResult);
                else
                    baseDao.update(checkResult);
                returnCode.setSuccess("操作成功");
                return (JSON)JSON.toJSON(returnCode);
            }catch (Exception e){
                returnCode.setFail("操作失败");
                return (JSON)JSON.toJSON(returnCode);
            }
        }
    }

    /**
     * 扫码日志
     * @param uid 用户id
     * @param code 字符串
     * @param timestamp 时间戳
     * @return
     */
    @RequestMapping(value = "/QrLog",method = RequestMethod.POST)
    @ResponseBody
    public JSON QrLog(Long uid, String code, Long timestamp){
        ReturnCode returnCode=new ReturnCode();
        if(uid==null||code==null||timestamp==null){
            returnCode.setFail("参数不能为空");
            return (JSON)JSON.toJSON(returnCode);
        }
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
            return (JSON)JSON.toJSON(returnCode);
        }else{
            try {
                QrLog qrLog = new QrLog();
                qrLog.setCode(code);
                qrLog.setTimestamp(timestamp);
                baseDao.save(qrLog);
                returnCode.setSuccess("操作成功");
                return (JSON)JSON.toJSON(returnCode);
            }catch (Exception e){
                returnCode.setFail("操作失败");
                return (JSON)JSON.toJSON(returnCode);
            }
        }
    }
}
