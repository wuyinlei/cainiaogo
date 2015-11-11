package com.example.ruolan.cainiaogo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.HomeCategory;

import java.util.List;


/**
 * Created by ruolan on 2015/11/11.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {


    private List<HomeCategory> mDatas;

    //构造函数，初始化list
    public HomeCategoryAdapter(List<HomeCategory> datas) {
        mDatas = datas;
    }

    private LayoutInflater mInflater;

    //定义两个常量，用来判断是否左右显示
    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    /**
     * 相当于listview中的getView方法
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化布局
        mInflater = LayoutInflater.from(parent.getContext());

        //判断显示的左右
        if (viewType == VIEW_TYPE_R) {
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2, parent, false));
        }
        return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview, parent, false));
    }


    @Override
    public long getItemId(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        } else
            return VIEW_TYPE_L;
    }

    /**
     * 进行数据的绑定
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeCategory category = mDatas.get(position);
       // holder.textTitle.setText(category.getName());
        Log.d("hodsa", category.getName().toString());
        holder.imageViewBig.setImageResource(category.getImgBig());
        holder.imageViewSmallTop.setImageResource(category.getImgSmallTop());
        holder.imageViewSmallBottom.setImageResource(category.getImgSmallBottom());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{


        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
           // textTitle = (TextView) itemView.findViewById(R.id.title_text);
            textTitle = (TextView) itemView.findViewById(R.id.title_text);

        }

    }
}
