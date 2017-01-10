package com.springapp.dao;

import com.springapp.entity.Account;
import org.springframework.stereotype.Repository;

/**
 * Created by 11369 on 2016/10/6.
 */
@Repository
public class AccountDao extends BaseDao {
    public Account loginCheck(String username){
        return this.find("from Account where username=?",Account.class,new Object[]{username});
    }
    public Account getAccountByToken(String token){
        return this.find("from Account where token=?",Account.class,new Object[]{token});
    }
}
