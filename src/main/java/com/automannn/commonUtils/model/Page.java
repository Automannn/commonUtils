package com.automannn.commonUtils.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author automannn@163.com
 * @time 2019/4/29 11:07
 */
public class Page<T> implements Serializable{

    private Long total;

    private List<T> data;

    public Page(Long total, List<T> data){
        this.total = total;
        this.data = data;
    }

    public static <T> Page<T> empty() {
        return new Page<T>(0L, Collections.<T>emptyList());
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", data=" + data +
                '}';
    }
}
