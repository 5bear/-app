package com.springapp.mvc;


import com.springapp.dao.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by anc on 15/3/13.
 */
public class BaseController {
    @Autowired
    protected BaseDao baseDao;
    @Autowired
    protected AccountDao accountDao;
    @Autowired
    protected AgentDao agentDao;
    @Autowired
    protected LogisticsDao logisticsDao;
    @Autowired
    protected QrcodeDao qrcodeDao;
    @Autowired
    protected GoodsDao goodsDao;
    @Autowired
    protected RelateCodeDao relateCodeDao;
}
