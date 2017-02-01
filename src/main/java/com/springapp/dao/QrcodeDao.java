package com.springapp.dao;

import com.springapp.entity.CheckResult;
import org.springframework.stereotype.Repository;

/**
 * Created by 11369 on 2016/10/26.
 */
@Repository
public class QrcodeDao extends BaseDao{
    public CheckResult getByPcode(String pcode){
        String hql = "from CheckResult where pcode = '" + pcode + "'";
        return this.find(hql, CheckResult.class, new Object[]{pcode});
    }
}
