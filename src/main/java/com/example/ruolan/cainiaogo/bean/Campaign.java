package com.example.ruolan.cainiaogo.bean;

import java.io.Serializable;

/**
 * Created by ruolan on 2015/11/13.
 */
public class Campaign implements Serializable {

    private long id;
    private String title;
    private String imgUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
