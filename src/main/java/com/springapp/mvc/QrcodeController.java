package com.springapp.mvc;

import com.alibaba.fastjson.JSON;
import com.springapp.classes.Base64Image;
import com.springapp.classes.ReturnCode;
import com.springapp.entity.Account;
import com.springapp.entity.Qrcode;
import com.springapp.entity.SuccessRate;
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
     * 上传二维码  UploadQrCode  post
     * @param session
     * @param uid Long 用户id
     * @param base64Image String 图片base64编码
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public JSON UploadQrCode(HttpSession session,/*String token*/Long uid,String base64Image){
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
     * 识别率 SuccessQrCode post
     * @param uid
     * @param successRate Float 成功率
     * @return
     */
    @RequestMapping(value = "/SuccessQrCode",method = RequestMethod.POST)
    @ResponseBody
    public JSON SuccessQrCode(/*String token*/Long uid,Float successRate){
        ReturnCode returnCode=new ReturnCode();
        if(uid==null||successRate==null){
            returnCode.setFail("参数不能为空");
            return (JSON)JSON.toJSON(returnCode);
        }
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
            return (JSON)JSON.toJSON(returnCode);
        }else{
            try {
                SuccessRate rate=new SuccessRate();
                rate.setUid(account.getId());
                rate.setRecordTime(System.currentTimeMillis());
                rate.setRate(successRate);
                baseDao.update(rate);
                returnCode.setSuccess("操作成功");
                return (JSON)JSON.toJSON(returnCode);
            }catch (Exception e){
                returnCode.setFail("操作失败");
                return (JSON)JSON.toJSON(returnCode);
            }
        }
    }
}
