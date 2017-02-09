package com.springapp.classes;

import com.springapp.DBUtil.JobDao;
import com.springapp.entity.*;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 11369 on 2017/1/4.
 */
public class ToExcel {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static String outExcel(JobDao baseDao) throws ParseException, SQLException {
        List<Map> mapList=new ArrayList<Map>();
        List<Logistics> logisticsList = baseDao.logisticsList();
        for(Logistics logistics:logisticsList){
            Map map=new HashMap();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            map.put("lCode", logistics.getlCode());
            map.put("createTime",logistics.getCreateTime());
            map.put("formatTime" , sdf.format(logistics.getCreateTime()));
            RelateCode relateCode = baseDao.getRelate(logistics.getlCode());
            if(relateCode != null){
                map.put("pCode", relateCode.getpCode());
            }else{
                map.put("pCode", "");
            }
            Goods goods = baseDao.getGoods(logistics.getlCode());
            if(goods != null){
                map.put("gCode", goods.getgCode());
            }else{
                map.put("gCode", "");
            }
            map.put("gType", "");
            map.put("gTypeInfo", "");
            Agent agent = baseDao.getAgent(logistics.getAid());
            if(agent != null){
                map.put("agent", agent.getAgent());
            }else{
                map.put("agent", "");
            }
            map.put("agentNo", "");
            Account account = baseDao.getAccount(logistics.getUid());
            if(account != null){
                map.put("dcName", account.getDcName());
            }else{
                map.put("dcName", "");
            }
            map.put("username", "");
            map.put("company", "");
            mapList.add(map);
        }
        try{
            String[] titles = new String[]{"托盘号","箱码（无托盘码，可填箱码）", "内码", "始发方代码","始发方名称","客户代码","客户名称","产品代码","产品名称","发运时间","物流公司(可填)","时间戳"};
            String[] keys = new String[]{"pCode","lCode", "gCode", "username","dcName","agentNo","agent","gType","gTypeInfo","formatTime","company","createTime"};

            HSSFWorkbook wb = new HSSFWorkbook();
            // 在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("sheet1");
            // 在sheet中添加表头第0行
            HSSFRow row = sheet.createRow((int) 0);
            // 创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle headStyle = wb.createCellStyle();
            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            headStyle.setBorderBottom((short) 1);
            headStyle.setBorderLeft((short) 1);
            headStyle.setBorderTop((short) 1);
            headStyle.setBorderRight((short) 1);

            HSSFRow firstRow = sheet.createRow((int) 0);
            HSSFCell cell;
            int i = 0;
            for (String title : titles) {
                cell = firstRow.createCell((short) i);
                cell.setCellValue(title);
                cell.setCellStyle(headStyle);
                i++;
            }
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            i = 1;



            for (Map map : mapList) {
                row = sheet.createRow(i);

                row.setRowStyle(style);
                int j = 0;
                //for(int n=0;i<enrollCodeList.size();n++)
                for (String key : keys) {
                    cell = row.createCell(j);
                    cell.setCellValue(map.get(key)+"");
                    cell.setCellStyle(style);
                    j++;
                }
                i++;
            }
            for(i = 0; i<titles.length; i++){
                sheet.autoSizeColumn(i);
            }
            String fileName = format.format(new Date()) + ".xls";
            String path = System.getProperty("user.dir") + "/" + "excels";
            File file = new File(path);
            if(!file.exists())
                file.mkdirs();
            String target = path + "/" +fileName;
            FileOutputStream o=null;
            try {
                //输出文件
                o = new FileOutputStream(target);
                wb.write(o);
                o.close();
                return target;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return "fail";
            }
            /*response.reset();
            // 设置response的Header
            response.setHeader("pragma", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + "result" + ".xls");
            response.setContentType("application ");
            try {
                wb.write(response.getOutputStream());
                response.flushBuffer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }*/
        }
        catch (Exception e){
            System.out.print("exception"+e);
            return "fail";
        }
    }

    public List<Map<String, String>> analysisfile(String path){

        List<Map<String, String>> result=new ArrayList<Map<String, String>>();
        Map<String,String> key=new HashMap<String, String>();

        try {
            File f = new File(path);
            InputStream in = new FileInputStream(f);

            HSSFWorkbook wb=new HSSFWorkbook(in);

            HSSFSheet hssfSheet=wb.getSheetAt(0);
            HSSFRow firstRow=hssfSheet.getRow(hssfSheet.getFirstRowNum());

            int iii=firstRow.getLastCellNum();
            for (int cellCount=firstRow.getFirstCellNum();cellCount<firstRow.getLastCellNum();cellCount++){
                HSSFCell cell=firstRow.getCell(cellCount);
                String value=cell.toString();
                key.put("" + cellCount, value);

            }

            for(int j=hssfSheet.getFirstRowNum()+1;j<=hssfSheet.getLastRowNum();j++){
                HSSFRow row=hssfSheet.getRow(j);
                Map<String,String> oneRow=new HashMap<String, String>();
                boolean IsEmpty=true;
                for (int cellCount=row.getFirstCellNum();cellCount<row.getLastCellNum();cellCount++){
                    HSSFCell cell=row.getCell(cellCount);
                    if(cell!=null) {
                        String value = cell.toString();
                        if (value != null && !value.equals("")){
                            oneRow.put(key.get(cellCount + ""), value);
                            IsEmpty=false;
                        }

                    }
                }
                if(!IsEmpty){
                    result.add(oneRow);
                }

            }
        } catch (Exception e ) {

            e.printStackTrace();
        }


        return result;
    }//导入excel

    public static void main(String[] args) throws Exception {
        JobDao jobDao = new JobDao();
        String path = ToExcel.outExcel(jobDao);
        System.out.print(path);
       /* SendEmail.sendMessage(path);*/
            /*baseDao.deleteLogistics();*/
        jobDao.close();
    }
}
