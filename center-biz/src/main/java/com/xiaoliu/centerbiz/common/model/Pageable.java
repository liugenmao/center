package com.xiaoliu.centerbiz.common.model;

import java.io.Serializable;

/**
 * 分页接口
 *
 * @author xiaoliu
 */
public interface Pageable extends Serializable {

    /**
     * 获取当前第几页
     *
     * @return 第几页
     */
    int getPageNumber();

    /**
     * 获取每页条数
     *
     * @return 每页条数
     */
    int getPageSize();

    /**
     * 获取当前页开始位置
     *
     * @return 开始位置
     */
    int getOffset();
}
