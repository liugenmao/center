package com.xiaoliu.center.common.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 页面实现
 *
 * @author xiaoliu
 */
public class PageImpl<Vo> implements Page<Vo> {

    private Pageable pageable;
    private List<Vo> content = new ArrayList<>();
    private long total;

    public PageImpl(List<Vo> content, Pageable pageable, long total) {
        if (null == content) {
            throw new IllegalArgumentException("数据不能为空!");
        }
        this.content.addAll(content);
        this.total = total;
        this.pageable = pageable;
    }

    @Override
    public int getPageNumber() {
        return pageable == null ? 0 : pageable.getPageNumber();
    }

    @Override
    public int getPageSize() {
        return pageable == null ? 0 : pageable.getPageSize();
    }

    @Override
    public int getPageCount() {
        return getPageSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getPageSize());
    }

    @Override
    public long getTotalCount() {
        return total;
    }

    @Override
    public List<Vo> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public boolean hasNext() {
        return getPageCount() != getPageNumber();
    }


    @Override
    public long getTotalElements() {
        return getTotalCount();
    }

    @Override
    public int getTotalPages() {
        return getPageCount();
    }

    @Override
    public boolean getLast() {
        return getPageCount() == getPageNumber();
    }

    @Override
    public boolean getFirst() {
        return getPageNumber() == 1;
    }

    @Override
    public int getSize() {
        return getPageSize();
    }

    @Override
    public int getNumber() {
        return getPageNumber();
    }
}
