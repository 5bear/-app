package com.springapp.dao;

import com.springapp.entity.Goods;
import org.springframework.stereotype.Repository;

/**
 * Created by 11369 on 2016/12/28.
 */
@Repository
public class GoodsDao extends BaseDao {
    public void truncateTable(){
        String sql = "TRUNCATE TABLE Goods";
        this.excuteSql(sql);
    }

    public Goods isIn(String gCode){
        return this.find("from Goods where gCode=?", Goods.class, new Object[]{gCode});
    }

}
