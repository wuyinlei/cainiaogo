package com.example.ruolan.cainiaogo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.adapter.HomeCategoryAdapter;
import com.example.ruolan.cainiaogo.bean.HomeCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruolan on 2015/11/11.
 */
public class HomeFragment extends Fragment {

    private HomeCategoryAdapter mAdapter;
    private HomeCategory category;

    private static final String TAG = "HomeFragment";

    //创建控件
    private SliderLayout mSliderLayout;
    private PagerIndicator mIndicator;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //找到SliderLayout的实例通过findViewById
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        mIndicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);

        //CardView效果初始化
        initRecycleView(view);

        //图片轮转效果初始化
        initSlider();

        return view;
    }

    //主页中部的图片加载的一个滑动，商品的推荐
    private void initRecycleView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        List<HomeCategory> datas = new ArrayList<>(15);

        //添加几个示例图，这个是固定的，还没有实现从网络中加载数据
        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    public void initSlider() {
        //创建slider
        TextSliderView textSliderView = new TextSliderView(this.getActivity());
        textSliderView.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t2416/102/20949846/13425/a3027ebc/55e6d1b9Ne6fd6d8f.jpg");
        textSliderView.description("新品推荐");
        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(getActivity(), "新品推荐", Toast.LENGTH_SHORT).show();
            }
        });

        TextSliderView textSliderView2 = new TextSliderView(this.getActivity());
        textSliderView2.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1507/64/486775407/55927/d72d78cb/558d2fbaNb3c2f349.jpg");
        textSliderView2.description("时尚男装");
        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(getActivity(), "时尚男装", Toast.LENGTH_SHORT).show();
            }
        });

        TextSliderView textSliderView3 = new TextSliderView(this.getActivity());
        textSliderView3.image("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1363/77/1381395719/60705/ce91ad5c/55dd271aN49efd216.jpg");
        textSliderView3.description("家电秒杀");
        textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(getActivity(), "家电秒杀", Toast.LENGTH_SHORT).show();
            }
        });

        //添加进sliderShow()
        mSliderLayout.addSlider(textSliderView);
        mSliderLayout.addSlider(textSliderView2);
        mSliderLayout.addSlider(textSliderView3);

        //设置动画效果
        // mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);   //系统默认的
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
}
