package com.springapp.classes;

import com.springapp.DBUtil.JobDao;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by 11369 on 2017/1/16.
 */
public class myJob2 extends QuartzJobBean {
    @Override
    protected void executeInternal(org.quartz.JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobDao jobDao = new JobDao();
            String path = ToExcel.outExcel(jobDao);
            SendEmail.sendMessage(path, "物流信息");
            path = ToExcel.outExcel2(jobDao);
            SendEmail.sendMessage(path, "箱垛关联");
            /*baseDao.deleteLogistics();*/
            jobDao.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
