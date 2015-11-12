package com.example.ruolan.cainiaogo.bean;

/**
 * Created by ruolan on 2015/11/12.
 */

import java.util.List;

/**
 * "totalCount":28,
 * "currentPage":1,
 * "totalPage":3,"
 * pageSize":10,
 * "list
 * @param <T>
 */
public class Page<T> {

    private int currentPage;
    private int pageSize;
    private int totalPage;
    private int totalCount;

    private List<T> list;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
