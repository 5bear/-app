package com.springapp.dao;

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

}
