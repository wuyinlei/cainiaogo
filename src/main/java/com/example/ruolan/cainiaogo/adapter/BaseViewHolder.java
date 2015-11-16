package com.example.ruolan.cainiaogo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ruolan on 2015/11/13.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    //定义一个数组用来存放View对象
    private SparseArray<View> views;

    protected BaseAdapter.OnItemClickListener mListener;

    public BaseViewHolder(View itemView,BaseAdapter.OnItemClickListener listener) {
        super(itemView);

        views = new SparseArray<View>();
        this.mListener = listener;
        itemView.setOnClickListener(this);
    }

    //对TextView的封装
    public TextView getTextView(int id){
        return findView(id);
    }

    //对ImageView的封装
    public ImageView getImageView(int id){
        return findView(id);
    }

    //对Button的封装
    public Button getButton(int id){
        return findView(id);
    }

    //对View的封装
    public View getView(int id){
        return findView(id);
    }

    private <T extends View> T findView(int id){
        View view = views.get(id);
        if (view == null){
            view = itemView.findViewById(id);
            views.put(id,view);
        }
        return (T) view;
    }

    //对ItemClick的设置的点击事件
    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.OnItemClick(v, getLayoutPosition());
        }
    }
}

