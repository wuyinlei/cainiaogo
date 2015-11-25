package com.example.ruolan.cainiaogo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.activity.WareListActivity;
import com.example.ruolan.cainiaogo.adapter.HomeCategoryAdapter;
import com.example.ruolan.cainiaogo.bean.Banner;
import com.example.ruolan.cainiaogo.bean.Campaign;
import com.example.ruolan.cainiaogo.bean.HomeCampaign;
import com.example.ruolan.cainiaogo.bean.HomeCategory;
import com.example.ruolan.cainiaogo.http.BaseCallback;
import com.example.ruolan.cainiaogo.http.OkHttpHelper;
import com.example.ruolan.cainiaogo.http.SpotsCallback;
import com.example.ruolan.cainiaogo.uri.Contants;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by ruolan on 2015/11/11.
 */
public class HomeFragment extends Fragment {

    private HomeCategoryAdapter mAdapter;


    private HomeCategory category;

    //创建一个gson实例
    private Gson mGson = new Gson();

    //用一个list数组来存放gson数据对象
    private List<Banner> mBanners;


    private static final String TAG = "HomeFragment";

    //创建控件
    private SliderLayout mSliderLayout;
    private PagerIndicator mIndicator;
    private RecyclerView mRecyclerView;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //找到SliderLayout的实例通过findViewById
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        mIndicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        requestImages();
        //CardView效果初始化
        initRecycleView(view);

        return view;
    }

    private void requestImages() {
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";


        httpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanners = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    //主页中部的图片加载的一个滑动，商品的推荐
    private void initRecycleView(View view) {

        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

      /*  mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        List<HomeCategory> datas = new ArrayList<>(15);

        //添加几个示例图，这个是固定的，还没有实现从网络中加载数据
        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2);
        datas.add(category);

        category = new HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2);
        datas.add(category);

        category = new HomeCategory("品牌街", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2);
        datas.add(category);

        category = new HomeCategory("金融街 包赚翻", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2);
        datas.add(category);

        category = new HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2);
        datas.add(category);

        mAdapter = new HomeCategoryAdapter(datas);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));*/

    }

    private void initData(List<HomeCampaign> homeCampaigns) {

        mAdapter = new HomeCategoryAdapter(homeCampaigns,getActivity());
        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                //Toast.makeText(getContext(),"点击我了哈",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    /**
     * 用来解析gson数据，初始化轮番广告
     */
    public void initSlider() {

        if (mBanners != null) {
            for (Banner banner : mBanners) {
                //创建sliderView
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                //添加进sliderShow()
                mSliderLayout.addSlider(textSliderView);
            }
        }

        //设置动画效果
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);   //系统默认的
        //接下来使用自定义的
        mSliderLayout.setCustomIndicator(mIndicator);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        //3秒的时间
        mSliderLayout.setDuration(3000);

        //滑动图片的监听事件
        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged");
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSliderLayout.stopAutoCycle();
    }
}
