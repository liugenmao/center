package com.xiaoliu.center.biz.sc.service.impl;

import com.xiaoliu.center.biz.sc.dao.ISCStockDao;
import com.xiaoliu.center.biz.sc.service.ISCStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SCStockServiceImpl implements ISCStockService {

    @Resource
    private ISCStockDao stockDao;

    @Override
    public int saveSCStock(String stockNo) throws RuntimeException {
        return stockDao.saveSCStock(stockNo);
    }
}
