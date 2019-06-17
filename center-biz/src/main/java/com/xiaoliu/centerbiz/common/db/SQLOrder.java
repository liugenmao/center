package com.xiaoliu.centerbiz.common.db;

/**
 * 数据库查询排序枚举类
 *
 * @author xiaoliu
 */
public enum SQLOrder {

    /**
     * ASC 升序, DESC 降序
     */
    ASC("asc"), DESC("desc");

    private String value;

    SQLOrder(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
