package com.example.ruolan.cainiaogo.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ruolan on 2015/11/12.
 */
public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.ViewHolder>{

    private List<Wares> mDatas;

    private LayoutInflater mInflater;

    public HotWaresAdapter(List<Wares> wares){
        mDatas = wares;
    }

    /**
     * 相当于listview中的getView()方法
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        /**
         * 初始化位置错误
         Error inflating class com.facebook.drawee.view.SimpleDraweeView
         android.view.InflateException: Binary XML file line #14: Error inflating class com.facebook.drawee.view.SimpleDraweeView
         Caused by: java.lang.reflect.InvocationTargetException
         Caused by: java.lang.NullPointerException
         at com.facebook.drawee.view.DraweeView.setImageDrawable(DraweeView.java:140)
         这里是没有引用到`com.facebook.drawee.view.SimpleDraweeView
         1.解决办法
         把初始化Fresco调整到setContentView(R.layout.activity_main);上边
         */
        Fresco.initialize(parent.getContext());
        View view = mInflater.inflate(R.layout.template_hot_wares,parent,false);
        return new ViewHolder(view);
    }

    /**
     * bind
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wares wares = getData(position);

        /**
         * 对布局中的控件赋值
         */
        holder.mDraweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.textTitle.setText(wares.getName());
        holder.textPrice.setText("￥" + wares.getPrice());
    }

    /**
     * 清楚数据的方法
     */
    public void cleanData(){
        mDatas.clear();
        notifyItemRangeRemoved(0, mDatas.size());
    }


    /**
     * 添加数据的方法
     * @param datas
     */
    public void addData(List<Wares> datas){
        addData(0, datas);
    }

    /**
     * 返回datas的长度
     */
    public List<Wares> getData() {
        return mDatas;
    }

    public void addData(int position,List<Wares> wares){

        if (wares != null &&wares.size() > 0) {
            mDatas.addAll(wares);

            notifyItemRangeChanged(0, mDatas.size());
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0){
            return mDatas.size();
        }
        return 0;
    }

    public Wares getData(int position){
        return mDatas.get(position);
    }

    public List<Wares> getDatas() {
        return mDatas;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

         //定义所需要的各种控件
         SimpleDraweeView mDraweeView;
         TextView textTitle;
         TextView textPrice;

         public ViewHolder(View itemView) {
             super(itemView);

             //绑定布局中的控件
             mDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
             textTitle = (TextView) itemView.findViewById(R.id.text_title);
             textPrice = (TextView) itemView.findViewById(R.id.text_price);
         }
     }
}
