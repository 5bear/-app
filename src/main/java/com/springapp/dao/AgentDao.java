package com.springapp.dao;

import com.springapp.classes.Guid;
import com.springapp.entity.Agent;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 11369 on 2016/10/6.
 */
@Repository
public class AgentDao extends BaseDao {
    public Agent addAgent(Long uid,String agent){
        Agent agent1=this.find("from Agent where agent=? and aid=?",Agent.class,new Object[]{agent,uid});
        if(agent1!=null)
            return null;
        Agent agent2=new Agent();
        agent2.setUid(uid);
        agent2.setAgent(agent);
        this.save(agent2);
        return agent2;
    }
    public Agent updateAgent(String guid,String agent){
        Agent agent1=this.find("from Agent where guid=?",Agent.class,new Object[]{guid});
        if(agent1==null)
            return null;
        Agent agent2=this.find("from Agent where guid!=? and agent=?",Agent.class,new Object[]{guid,agent});
        if(agent2!=null)
            return null;
        agent1.setAgent(agent);
        this.update(agent1);
        return agent1;
    }
    public Agent deleteAgent(String guid){
        Agent agent1=this.find("from Agent where guid=?",Agent.class,new Object[]{guid});
        if(agent1==null)
            return null;
        this.delete(agent1);
        return agent1;
    }
    public List<Agent>list(Long uid){
        return this.findAllBySql("select * from Agent order by convert(agent USING gbk) COLLATE gbk_chinese_ci asc",Agent.class);
    }
    public List<Agent>AgentPageList(Long uid,int pn,int length){
        if(pn<=0)
            pn=1;
        if(length<=0)
            length=0;
        return this.findSQLByPage("select * from Agent order by convert(agent USING gbk) COLLATE gbk_chinese_ci asc ",Agent.class,(pn-1)*length,length);
    }
}
