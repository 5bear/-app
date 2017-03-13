package com.springapp.mvc;

import com.alibaba.fastjson.JSON;
import com.springapp.classes.*;
import com.springapp.entity.Account;
import com.springapp.entity.Goods;
import com.springapp.entity.RelateCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * Created by 11369 on 2016/12/27.
 */
@Controller
@RequestMapping(value = "Goods")
public class GoodsController extends BaseController {
    /**
     * 生成标签 输出的是 垛数×（箱数张一样的+空白数张空白），和一个图片文件列表，可以txt格式
     * @param gType 产品类型缩写 用于生成串码
     * @param gTypeInfo  产品类型名称 用于导出excel
     * @param pallets 垛数
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(Integer rgb, String gType, String gTypeInfo,Integer pallets,HttpSession session, HttpServletResponse response) throws Exception {
        ReturnCode returnCode = new ReturnCode();
        if(gType==null || "".equals(gType)){
            returnCode.setFail("产品类型缩写不能为空");
            return JSON.toJSONString(returnCode);
        }
        if(gTypeInfo==null || "".equals(gTypeInfo)){
            returnCode.setFail("产品类型名称不能为空");
            return JSON.toJSONString(returnCode);
        }
        ArrayList<String> gCodes = new ArrayList<String>();
        ArrayList<String> lCodes = new ArrayList<String>();
        Integer count = pallets;
        while (count>0){
            Long time = System.currentTimeMillis();
            Goods goods = new Goods();
            String gCode = generateGcode(gType);
            Goods tmp = goodsDao.isIn(gCode);
            while (tmp!=null){
                System.out.print("重复");
                gCode = generateGcode(gType);
                tmp = goodsDao.isIn(gCode);
            }
            String salt = time.toString();
            String lCode = MD5.MD5Encode(gCode + salt);
            gCodes.add(gCode);
            lCodes.add(lCode);
            goods.setgCode(gCode);
            goods.setSalt(salt);
            goods.setlCode(lCode);
            goods.setgType(gType);
            goods.setgTypeInfo(gTypeInfo);
            baseDao.save(goods);
            count--;
        }
        //获得要打包的文件夹路径
        String targetLabelPath = generateLabel2(session, rgb, gCodes, lCodes, pallets);
        //压缩包名
        String zipName = gType + System.currentTimeMillis() + ".zip";
        //压缩包存储位置
        String targetZipPath = session.getServletContext().getRealPath("/WEB-INF/pages/zips/");
        File file = new File(targetZipPath);
        //不存在要新建
        if(!file.exists())
            file.mkdirs();
        //压缩包绝对位置
        String zipPath = targetZipPath +"/" + zipName;
        FileUtil.zip(targetLabelPath, zipPath);
        File outFile = new File(zipPath); //要下载的文件绝对路径
        InputStream ins = new BufferedInputStream(new FileInputStream(outFile));
        byte [] buffer = new byte[ins.available()];
        ins.read(buffer);
        ins.close();
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(zipName.getBytes()));
        response.addHeader("Content-Length", "" + outFile.length());
        OutputStream ous = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        ous.write(buffer);
        ous.flush();
        ous.close();
        return JSON.toJSONString(returnCode);
    }


    /*

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(String gType, String gTypeInfo,Integer pallets, Integer realCount, Integer emptyCount ,HttpSession session, HttpServletResponse response) throws Exception {
        ReturnCode returnCode = new ReturnCode();
        if(gType==null || "".equals(gType)){
            returnCode.setFail("产品类型缩写不能为空");
            return JSON.toJSONString(returnCode);
        }
        if(gTypeInfo==null || "".equals(gTypeInfo)){
            returnCode.setFail("产品类型名称不能为空");
            return JSON.toJSONString(returnCode);
        }
        Goods goods = new Goods();
        String gCode = generateGcode(gType);
        Long time = System.currentTimeMillis();
        String salt = time.toString();
        String lCode = MD5.MD5Encode(gCode + salt);
        goods.setgCode(gCode);
        goods.setSalt(salt);
        goods.setlCode(lCode);
        goods.setgType(gType);
        goods.setgTypeInfo(gTypeInfo);
        baseDao.save(goods);
        //获得要打包的文件夹路径
        String targetLabelPath = generateLabel(session, gCode, lCode, pallets*realCount, pallets*emptyCount);
        //压缩包名
        String zipName = gType + System.currentTimeMillis() + ".zip";
        //压缩包存储位置
        String targetZipPath = session.getServletContext().getRealPath("/WEB-INF/pages/zips/");
        File file = new File(targetZipPath);
        //不存在要新建
        if(!file.exists())
            file.mkdirs();
        //压缩包绝对位置
        String zipPath = targetZipPath +"/" + zipName;
        FileUtil.zip(targetLabelPath, zipPath);
        File outFile = new File(zipPath); //要下载的文件绝对路径
        InputStream ins = new BufferedInputStream(new FileInputStream(outFile));
        byte [] buffer = new byte[ins.available()];
        ins.read(buffer);
        ins.close();
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(zipName.getBytes()));
        response.addHeader("Content-Length", "" + outFile.length());
        OutputStream ous = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        ous.write(buffer);
        ous.flush();
        ous.close();
        return JSON.toJSONString(returnCode);
    }
*/
    /**
     * 垛箱关联
     * @param lCode 箱码
     * @param pCode 垛码
     * @param uid   登录用户id
     * @return
     */
    @RequestMapping(value = "/relate",method = RequestMethod.POST)
    @ResponseBody
    public JSON relate(String lCode, String pCode, Long uid){
        ReturnCode returnCode=new ReturnCode();
        Account account= accountDao.get(Account.class, uid);
        if(account==null){
            returnCode.setFail("请首先登录");
        }else {
            if(lCode==null||lCode.equals("")||pCode==null||pCode.equals("")){
                returnCode.setFail("参数不能为空");
                return (JSON)JSON.toJSON(returnCode);
            }
            RelateCode relateCode = new RelateCode();
            relateCode.setUid(uid);
            relateCode.setlCode(lCode);
            relateCode.setpCode(pCode);
            relateCode.setTimestamp(System.currentTimeMillis());
            goodsDao.save(relateCode);
            /*if(relateCode!=null){
                relateCode.setUid(uid);
                relateCode.setpCode(pCode);
                relateCode.setTimestamp(System.currentTimeMillis());
                relateCodeDao.update(relateCode);
            }else {
                relateCode = new RelateCode();
                relateCode.setUid(uid);
                relateCode.setlCode(lCode);
                relateCode.setpCode(pCode);
                relateCode.setTimestamp(System.currentTimeMillis());
                goodsDao.save(relateCode);
            }*/
            returnCode.setSuccess(relateCode);
        }
        return (JSON)JSON.toJSON(returnCode);
    }
    public String generateGcode(String gType){
        /*Integer a[] = {1,2,3,4,5,6,7,9,1,2};*/
        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
//随机生成数字，并添加到字符串
        for(int i=0;i<8;i++){
            int x = random.nextInt(10);
            if(x==0)
                x = 2;
            if(x==8)
                x = 6;
            str.append(x);
        }
        int num=Integer.parseInt(str.toString());
//将字符串转换为数字并输出
        /*int num=Integer.parseInt(str.toString());
        System.out.println(num);
        int step = (num%10);
        int i=0;
        while((i+step)<10){
            int t = a[i];
            a[i] = a[i+step];
            a[i+step] = t;
            i++;
        }
        int pos ;
        str = new StringBuilder();
        while(num>0){
            pos = num % 10;
            str.append(a[pos]);
            num /= 10;
        }
        int randomNum=Integer.parseInt(str.toString());*/
        return gType + num;
        /*StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
        //随机生成数字，并添加到字符串
        for(int i=0;i<8;i++){
            str.append(random.nextInt(10));
        }
        //将字符串转换为数字并输出
        int num=Integer.parseInt(str.toString());
        return gType +num;*/
    }

    /**
     * 返回要压缩的文件名
     * @param session
     * @param gCode
     * @param lCode
     * @param realCount
     * @param emptyCount
     * @return
     * @throws IOException
     */
    public String generateLabel(HttpSession session,  String gCode, String lCode, Integer realCount, Integer emptyCount) throws IOException {
        Long time = System.currentTimeMillis();
        String srcImgPath = session.getServletContext().getRealPath("/WEB-INF/pages/static/template5.PNG");
        String targetLabelPath = session.getServletContext().getRealPath("/WEB-INF/pages/labels/" + time);//新建一个文件夹存储标签
        File file = new File(targetLabelPath);
        if(!file.exists())
            file.mkdirs();
        String targetQrPath = session.getServletContext().getRealPath("/WEB-INF/pages/qrcodes/" + time);
        file = new File(targetQrPath);
        if(!file.exists())
            file.mkdirs();
        Map<String, Stack<String>> map =  LabelUtil.generateLabel2(gCode, lCode, srcImgPath, targetQrPath, targetLabelPath, realCount, emptyCount);
        String fileName = System.currentTimeMillis()/1000 + ".txt";
        String targetTextPath = targetLabelPath + "/" +  fileName;//跟标签放在一起方便打包
        /*FileWriter fileWritter = new FileWriter(targetTextPath,false);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        for(String key:map.keySet()){
            Stack<String> stack = map.get(key);
            while (!stack.isEmpty()){
                bufferWritter.write(stack.pop());
                bufferWritter.newLine();
            }
        }
        bufferWritter.close();*/
        String content = "";
        for(String key:map.keySet()){
            Stack<String> stack = map.get(key);
            while (!stack.isEmpty()){
                content += stack.pop() + "\r\n";
            }
        }
        FileOutputStream o=null;
        try {
            //输出文件
            o = new FileOutputStream(targetTextPath);
            o.write(content.getBytes("GBK"));
            o.close();
            return targetLabelPath;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "fail";
        }
    }


    /**
     * 1.9修改
     * @param session
     * @param gCode
     * @param lCode
     * @param realCount
     * @return
     * @throws IOException
     */
    public String generateLabel2(HttpSession session, Integer rgb, ArrayList<String> gCode, ArrayList<String> lCode, Integer realCount) throws IOException {
        Long time = System.currentTimeMillis();
/*
        String srcImgPath = session.getServletContext().getRealPath("/WEB-INF/pages/static/template5.PNG");
*/
        String targetLabelPath = session.getServletContext().getRealPath("/WEB-INF/pages/labels/" + time);//新建一个文件夹存储标签
        File file = new File(targetLabelPath);
        if(!file.exists())
            file.mkdirs();
        String targetQrPath = session.getServletContext().getRealPath("/WEB-INF/pages/qrcodes/" + time);
        file = new File(targetQrPath);
        if(!file.exists())
            file.mkdirs();
        LabelUtil.setColor(rgb);
        Stack<String> stack =  LabelUtil.generateLabel3(gCode, targetLabelPath, realCount);
        String fileName = System.currentTimeMillis()/1000 + ".txt";
        String targetTextPath = targetLabelPath + "/" +  fileName;//跟标签放在一起方便打包
        /*FileWriter fileWritter = new FileWriter(targetTextPath,false);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        for(String key:map.keySet()){
            Stack<String> stack = map.get(key);
            while (!stack.isEmpty()){
                bufferWritter.write(stack.pop());
                bufferWritter.newLine();
            }
        }
        bufferWritter.close();*/
        String content = "内码\t箱码\t文件名\r\n";
        int i = realCount-1;
        while (!stack.isEmpty()){
            content += gCode.get(i) + "\t" + lCode.get(i) + "\t" + stack.pop() + "\r\n";
            i--;
        }
        FileOutputStream o=null;
        try {
            //输出文件
            o = new FileOutputStream(targetTextPath);
            o.write(content.getBytes("GBK"));
            o.close();
            return targetLabelPath;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "fail";
        }
    }

    public static void main(String[] args){
        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
//随机生成数字，并添加到字符串
        for(int i=0;i<8;i++){
            str.append(random.nextInt(10));
        }
//将字符串转换为数字并输出
        int num=Integer.parseInt(str.toString());
        System.out.println(num);
    }

}
