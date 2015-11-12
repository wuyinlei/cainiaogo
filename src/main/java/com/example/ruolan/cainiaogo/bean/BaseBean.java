package com.example.ruolan.cainiaogo.bean;

import java.io.Serializable;

/**
 * Created by ruolan on 2015/11/11.
 */

/**
 * 实现了Serializable接口，序列化
 */
public class BaseBean implements Serializable {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
