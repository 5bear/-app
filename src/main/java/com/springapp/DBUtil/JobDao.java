package com.springapp.DBUtil;



import com.springapp.classes.SendEmail;
import com.springapp.classes.ToExcel;
import com.springapp.entity.*;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 11369 on 2016/9/4.
 */
public class JobDao {
    private static String url="jdbc:mysql://localhost:3306/jw?characterEncoding=utf-8";
    private static String username="root";
    private static String password="root";
    private Connection connection;
    public JobDao() throws SQLException {
        this.connection=new JDBCUtil(url,username,password).getConnection();
    }
    public void close() throws SQLException {
        connection.close();
    }
    public void TruncateTable(String table) throws SQLException {
        String sql="TRUNCATE TABLE "+table;
        Statement statement=connection.createStatement();
        statement.executeUpdate(sql);
    }

    public void insertGoods(List<Goods> goodses) throws SQLException {
        for(Goods goods:goodses){
            String sql = "insert into Goods(gCode, lCode, salt)  values(" + "'" + goods.getgCode() + "'," + "'" + goods.getlCode() + "'," + "'" + goods.getSalt() +"')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
    }


    /**
     * 获得所有物流信息
     * @return
     * @throws SQLException
     */
    public List<Logistics> logisticsList() throws SQLException {
        List<Logistics> logisticsList = new ArrayList<Logistics>();
        String sql="select uid,aid,lCode,createTime from Logistics";
        PreparedStatement preState=connection.prepareStatement(sql);
        ResultSet rs=preState.executeQuery();
        while (rs.next()) {
            Logistics logistics = new Logistics();
/*
            logistics.setId(rs.getLong("id"));
*/
            logistics.setUid(rs.getLong("uid"));
            logistics.setAid(rs.getLong("aid"));
            logistics.setlCode(rs.getString("lCode"));
/*
            logistics.setQrTime(rs.getString("qrTime"));
*/
            logistics.setCreateTime(rs.getLong("createTime"));
/*
            logistics.setRemark1(rs.getString("remark1"));
*/
/*
            logistics.setRemark2(rs.getString("remark2"));
*/
/*
            logistics.setRemark3(rs.getString("remark3"));
*/
            logisticsList.add(logistics);
        }
        return logisticsList;
    }

    /**
     * 删除三天前数据
     */
    public void deleteLogistics() throws ParseException, SQLException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = format1.format(new Date());
        today += " 00:00:00";
        Date date = format2.parse(today);
        Long fromTime = date.getTime() - 1000*60*60*24*3;
        String sql = "delete from Logistics where createTime < " + fromTime;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
    /**
     * 获取用户信息
     * @param uid
     * @return
     * @throws SQLException
     */
    public Account getAccount(Long uid) throws SQLException {
        String sql = "select dcName,username from Account where id = " + uid;
        PreparedStatement preState = connection.prepareStatement(sql);
        ResultSet rs = preState.executeQuery();
        Account account = null;
        while(rs.next()) {
            account = new Account();
/*
            account.setId(rs.getLong("id"));
*/
            account.setDcName(rs.getString("dcName"));
/*
            account.setPassword(rs.getString("password"));
*/
/*
            account.setToken(rs.getString("token"));
*/
            account.setUsername(rs.getString("username"));
        }
        return account;
    }

    /**获得经销商信息
     * @param aid
     * @return
     * @throws SQLException
     */
    public Agent getAgent(Long aid) throws SQLException {
        String sql = "select agent,agentNo from Agent where id = " + aid;
        PreparedStatement preState = connection.prepareStatement(sql);
        ResultSet rs = preState.executeQuery();
        Agent agent = null;
        while(rs.next()) {
            agent = new Agent();
/*
            agent.setId(rs.getLong("id"));
*/
            agent.setAgent(rs.getString("agent"));
            agent.setAgentNo(rs.getString("agentNo"));
/*
            agent.setUid(rs.getLong("uid"));
*/
        }
        return agent;
    }

    /**
     * 获得商品信息
     * @param lCode
     * @return
     * @throws SQLException
     */
    public Goods getGoods(String lCode) throws SQLException {
        String sql = "select gCode,gType,gTypeInfo from Goods where lCode = '" +lCode +"'";
        PreparedStatement preState = connection.prepareStatement(sql);
        ResultSet rs = preState.executeQuery();
        Goods good = null;
        while (rs.next()){
            good = new Goods();
/*
            good.setId(rs.getLong("id"));
*/
            good.setgCode(rs.getString("gCode"));
            good.setgType(rs.getString("gType"));
            good.setgTypeInfo(rs.getString("gTypeInfo"));
/*
            good.setlCode(lCode);
*/
/*
            good.setSalt(rs.getString("salt"));
*/
        }
        return good;
    }

    /**
     * 获得箱垛关联
     * @param lCode
     * @return
     * @throws SQLException
     */
    public RelateCode getRelate(String lCode) throws SQLException {
        String sql = "select * from RelateCode where lCode = '" + lCode + "'";
        PreparedStatement preState = connection.prepareStatement(sql);
        ResultSet rs = preState.executeQuery();
        RelateCode relateCode = null;
        while (rs.next()){
            relateCode = new RelateCode();
            relateCode.setId(rs.getLong("id"));
            relateCode.setlCode(lCode);
            relateCode.setpCode(rs.getString("pCode"));
/*
            relateCode.setTimestamp(rs.getLong("timestamp"));
*/
        }
        return relateCode;
    }
    public static void main(String[]args) throws Exception {
        JobDao jobDao = new JobDao();
        String path = ToExcel.outExcel(jobDao);
        SendEmail.sendMessage(path);
            /*baseDao.deleteLogistics();*/
        jobDao.close();
    }
    /*public static void main(String []args) throws SQLException, IOException {
        BaseDao baseDao=new BaseDao();
        List<Account>accounts=baseDao.RecentAccountList();
        List<Slide>slideList=baseDao.Candidate_Items(accounts);
        for(Slide slide:slideList){
            System.out.print(slide.getsId()+"\n");
        }
    }*/
}
