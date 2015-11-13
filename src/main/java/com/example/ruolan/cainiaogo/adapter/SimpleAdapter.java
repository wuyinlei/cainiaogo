package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by ruolan on 2015/11/13.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {


    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    protected SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }

}
