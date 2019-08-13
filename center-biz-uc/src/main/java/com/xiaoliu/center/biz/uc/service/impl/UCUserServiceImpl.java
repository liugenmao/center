package com.xiaoliu.center.biz.uc.service.impl;

import com.xiaoliu.center.biz.uc.dao.IUCUserDao;
import com.xiaoliu.center.biz.uc.service.IUCUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UCUserServiceImpl implements IUCUserService {

    @Resource
    private IUCUserDao userDao;

    @Override
    public int saveUCUser(String userNo) throws RuntimeException {
        return userDao.saveUCUser(userNo);
    }
}
