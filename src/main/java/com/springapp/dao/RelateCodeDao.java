package com.springapp.dao;

import com.springapp.entity.RelateCode;
import org.springframework.stereotype.Repository;

/**
 * Created by 11369 on 2017/1/3.
 */
@Repository
public class RelateCodeDao extends BaseDao {
    public RelateCode getByLcode(String lCode){
        return this.find("from RelateCode where lCode = ?", RelateCode.class, new Object[]{lCode});
    }
}
