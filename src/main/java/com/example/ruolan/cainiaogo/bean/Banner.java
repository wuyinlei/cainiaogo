package com.example.ruolan.cainiaogo.bean;

/**
 * Created by ruolan on 2015/11/12.
 */
public class Banner extends BaseBean {

    private String name;
    private String imgUrl;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
