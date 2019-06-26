package com.xiaoliu.centerbiz.service;

import com.xiaoliu.centerbiz.common.result.Result;
import com.xiaoliu.centerbiz.domain.User;

public interface IUserService {

    User getUserByUsername(String username);

    Result updateUser(User user);
}
