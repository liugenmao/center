package com.xiaoliu.center.common.model;

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


    /**
     * 总共的数据条数
     */
    long getTotalElements();

    /**
     * 总页数
     */
    int getTotalPages();

    /**
     * 是否是最后一页
     */
    boolean getLast();

    /**
     * 是否是第一页
     */
    boolean getFirst();

    /**
     * 每页多少条数据
     */
    int getSize();

    /**
     * 当前第几页
     */
    int getNumber();

}
