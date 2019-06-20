package com.xiaoliu.centerbiz.service.impl;

import com.xiaoliu.centerbiz.dao.IUserDao;
import com.xiaoliu.centerbiz.domain.User;
import com.xiaoliu.centerbiz.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserDao userDao;

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}
