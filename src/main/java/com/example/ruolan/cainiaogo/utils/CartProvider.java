package com.example.ruolan.cainiaogo.utils;

import android.content.Context;
import android.util.SparseArray;

import com.example.ruolan.cainiaogo.bean.ShoppingCart;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruolan on 2015/11/14.
 */
public class CartProvider {

    private SparseArray<ShoppingCart> datas = null;

    private Context mContext;

    private static final String CART_JSON = "catr_json";

    public CartProvider(Context context) {
        //默认初始化10
        datas = new SparseArray<>(10);
        ListToSprse();
        mContext = context;
    }

    //往购物车中添加数据
    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get((int) cart.getId());
        if (temp != null) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
        }
        datas.put((int) cart.getId(), temp);
        commit();
    }

    public void update(ShoppingCart cart) {
        datas.put((int) cart.getId(), cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        datas.delete((int) cart.getId());
        commit();
    }

    //对数据的保存,这里是存储到本地，当然也可以改成存储到云端
    public void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
    }

    //把SprseArray转换成List
    public List<ShoppingCart> sparseToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }

    public List<ShoppingCart> getAll() {

        //从本地读取数据
        return getDataFromLocal();
    }

    /**
     * 从本地保存的数据中读取出来，这里也可以改成从云端读取出来
     * @return
     */
    public List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtils.getString(mContext, CART_JSON);

        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return carts;
    }

    private void ListToSprse(){
        List<ShoppingCart> carts = getDataFromLocal();

        if (carts != null && carts.size() > 0){
            for (ShoppingCart cart:carts) {
                datas.put((int) cart.getId(),cart);
            }
        }
        
    }

}

