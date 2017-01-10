package com.springapp.dao;

import com.springapp.entity.Agent;
import com.springapp.entity.Logistics;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by 11369 on 2016/10/6.
 */
@Repository
public class LogisticsDao extends BaseDao{
    public Logistics addLogistics(Logistics logistics){
        Agent agent=this.find("from Agent where id=?",Agent.class,new Object[]{logistics.getAid()});
        if(agent==null)
            return null;
        this.save(logistics);
        return logistics;
    }
    public List<Logistics>list(Long uid){
        return this.findAllBySql("select * from Logistics where uid="+uid+" order by createTime desc",Logistics.class);
    }
    public List<Logistics>LogisticsPageList(Long uid, int pn, int length){
        if(pn<=0)
            pn=1;
        if(length<=0)
            length=0;
        return this.findSQLByPage("select * from Logistics where uid="+uid+" order by createTime desc",Logistics.class,(pn-1)*length,length);
    }

    public Logistics getByLcodeAndAid(String lCode, Long aid){
        return this.find("from Logistics where lCode = ? and aid = ?", Logistics.class, new Object[]{lCode,aid});
    }
}
