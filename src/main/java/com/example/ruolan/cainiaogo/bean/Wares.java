package com.example.ruolan.cainiaogo.bean;

import java.io.Serializable;

/**
 * Created by ruolan on 2015/11/12.
 */

/**
 * 商品bean
 * {"id":1,
 * "name":"联想（Lenovo）拯救者14.0英寸游戏本（i7-4720HQ 4G 1T硬盘 GTX960M 2G独显 FHD IPS屏 背光键盘）黑",
 * "imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55c1e8f7N4b99de71.jpg",
 * "description":null,
 * "price":5979.0,
 * "sale":8654}
 */
public class Wares implements Serializable {
    private long id;
    private String name;
    private String imgUrl;
    private String description;
    private Float price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
