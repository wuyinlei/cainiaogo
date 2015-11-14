package com.example.ruolan.cainiaogo.bean;

import java.io.Serializable;

/**
 * Created by ruolan on 2015/11/14.
 */
public class ShoppingCart extends Wares implements Serializable{

    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
