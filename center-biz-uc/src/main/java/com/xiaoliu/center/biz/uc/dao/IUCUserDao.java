package com.xiaoliu.center.biz.uc.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;

public interface IUCUserDao {

    @Insert("insert into uc_user (user_no) values (#{userNo})")
    @Options(useCache = false)
    @ResultType(Integer.class)
    int saveUCUser(String userNo);
}