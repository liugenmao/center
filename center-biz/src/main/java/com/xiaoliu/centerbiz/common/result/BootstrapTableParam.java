package com.xiaoliu.centerbiz.common.result;

/**
 * bootstrap table参数
 *
 * @author xiaoliu
 */
public class BootstrapTableParam {
    private Integer limit;
    private Integer offset;
    private String sort;
    private String order;
    private String search;

    public Integer getLimit() {
        return limit == null ? 10 : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset == null ? 1 : offset / limit + 1;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean getOrder() {
        return "asc".equalsIgnoreCase(order);
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
