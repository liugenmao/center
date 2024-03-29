package com.xiaoliu.center.common.model;

/**
 * 分页实现
 *
 * @author xiaoliu
 */
public class PageRequest implements Pageable {

    /**
     * 默认页长为10
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 默认第一页
     */
    private static final int DEFAULT_PAGE = 1;

    private int pageNumber;
    private int pageSize;

    public PageRequest() {
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.pageNumber = DEFAULT_PAGE;
    }

    public PageRequest( Integer pageNumber, Integer pageSize ) {
        if ( pageNumber == null || 0 >= pageNumber ) {
            this.pageNumber = DEFAULT_PAGE;
        } else {
            this.pageNumber = pageNumber;
        }
        if ( pageSize == null || 0 >= pageSize ) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
    }

    public void setPageSize( int pageSize ) {
        this.pageSize = pageSize;
    }

    public void setPageNumber( int pageNumber ) {
        this.pageNumber = pageNumber;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getOffset() {
        return ( pageNumber - 1 ) * pageSize;
    }

}
