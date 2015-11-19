package com.example.ruolan.cainiaogo.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.Campaign;
import com.example.ruolan.cainiaogo.bean.HomeCampaign;
import com.squareup.picasso.Picasso;

import java.util.List;



/**
 * Created by ruolan on 2015/11/11.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {


    private List<HomeCampaign> mDatas;
    private Context mContext;

    //构造函数，初始化list
    public HomeCategoryAdapter(List<HomeCampaign> datas) {
        mDatas = datas;
    }

    private LayoutInflater mInflater;
    private OnCampaignClickListener mListener;

    public void setOnCampaignClickListener(OnCampaignClickListener listener) {

        this.mListener = listener;
    }

    //定义两个常量，用来判断是否左右显示
    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    public HomeCategoryAdapter(List<HomeCampaign> homeCampaigns, Context context) {
        mDatas = homeCampaigns;
        mContext = context;
    }

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
    public int getItemViewType(int position) {
        if (position % 2 == 0)
            return VIEW_TYPE_R;
        else
            return VIEW_TYPE_L;
    }


    /**
     * 进行数据的绑定
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*HomeCategory category = mDatas.get(position);
        // holder.textTitle.setText(category.getName());
        holder.imageViewBig.setImageResource(category.getImgBig());
        holder.imageViewSmallTop.setImageResource(category.getImgSmallTop());
        holder.imageViewSmallBottom.setImageResource(category.getImgSmallBottom());*/
        HomeCampaign homeCampaign = mDatas.get(position);

        holder.textTitle.setText(homeCampaign.getTitle());
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                aniv(v);
            }
        }

        private void aniv(final View v) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {

                    HomeCampaign campaign = mDatas.get(getLayoutPosition());

                    switch (v.getId()) {

                        case R.id.imgview_big:
                            mListener.onClick(v, campaign.getCpOne());
                            break;

                        case R.id.imgview_small_top:
                            mListener.onClick(v, campaign.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mListener.onClick(v, campaign.getCpThree());
                            break;

                    }

                }
            });
            animator.start();
        }
    }

    public interface OnCampaignClickListener {
        void onClick(View view, Campaign campaign);
    }
}
