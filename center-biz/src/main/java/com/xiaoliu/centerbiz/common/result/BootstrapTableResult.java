package com.xiaoliu.centerbiz.common.result;

import java.util.List;

/**
 * bootstrap table返回对象
 *
 * @author xiaoliu
 */
public class BootstrapTableResult {
    /**
     * 总记录数
     */
    private long total;
    /**
     * 数据
     */
    private List<?> rows;

    public BootstrapTableResult() {
    }

    public BootstrapTableResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
