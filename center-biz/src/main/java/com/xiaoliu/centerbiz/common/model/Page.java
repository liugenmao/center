package com.xiaoliu.centerbiz.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * 页面接口
 *
 * @author xiaoliu
 */
public interface Page<Vo> extends Serializable {

    /**
     * 获取第几页
     *
     * @return 第几页
     */
    int getPageNumber();

    /**
     * 获取页长
     *
     * @return 页长
     */
    int getPageSize();

    /**
     * 获取总条数
     *
     * @return 总条数
     */
    long getTotalCount();

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    int getPageCount();

    /**
     * 获取页面内容
     *
     * @return 页面内容
     */
    List<Vo> getContent();

    /**
     * 是否有下一页
     *
     * @return 是/否
     */
    boolean hasNext();

}
