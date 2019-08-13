package com.xiaoliu.center.biz.sc.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;

public interface ISCStockDao {

    @Insert("insert into sc_stock (stock_no) values (#{stockNo})")
    @Options(useCache = false)
    @ResultType(Integer.class)
    int saveSCStock(String stockNo);
}
