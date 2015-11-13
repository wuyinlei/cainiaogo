package com.example.ruolan.cainiaogo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.adapter.BaseAdapter;
import com.example.ruolan.cainiaogo.adapter.CategoryAdapter;
import com.example.ruolan.cainiaogo.adapter.DividerGridItemDecoration;
import com.example.ruolan.cainiaogo.adapter.DividerItemDecoration;
import com.example.ruolan.cainiaogo.adapter.WareAdapter;
import com.example.ruolan.cainiaogo.bean.Banner;
import com.example.ruolan.cainiaogo.bean.Category;
import com.example.ruolan.cainiaogo.bean.Page;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.example.ruolan.cainiaogo.http.BaseCallback;
import com.example.ruolan.cainiaogo.http.OkHttpHelper;
import com.example.ruolan.cainiaogo.http.SpotsCallback;
import com.example.ruolan.cainiaogo.uri.Contants;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by ruolan on 2015/11/11.
 */
public class CategoryFragment extends Fragment {

    //当前页数
    private int curPage = 1;

    //每页最大的数量
    private int pageSize = 10;

    //总的页数
    private int totalPage;


    //定义三种状态，分别是刷新之前，刷新中，加载更多
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int STATE = STATE_NORMAL;


    //定义布局文件中的控件
    private RecyclerView mWaresRecycleView;
    private RecyclerView mRecyclerView;
    //轮番炫酷广告
    private SliderLayout mSliderLayout;
    //刷新的控件
    private MaterialRefreshLayout mRefreshLayout;

    //创建一个wares的list的数据数组
    private List<Wares> datas;

    //定义需要用到的适配器
    private CategoryAdapter mCategoryAdapter;
    private WareAdapter wareAdapter;

    private long category_id = 0;

    //okhttp辅助类
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //绑定定义好的控件
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_category);

        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);

        mWaresRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview_wares);

        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);


        requestCategoryData();

        requestBannerData();

        initRefreshLayout();

        return view;
    }

    /**
     * 刷新方法，上拉加载更多，下拉刷新
     */
    private void initRefreshLayout() {

        mRefreshLayout.setLoadMore(true);  //调用此方法是加载更多

        //设置监听事件
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

            /**
             * 下拉刷新的方法
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            /**
             * 上拉加载更多的方法
             * @param materialRefreshLayout
             */
            //@Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (curPage <= totalPage) {  //判断是否是还有后继页，如果没有，就提示已经没有更多的内容加载了
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "已经是最后一页了，没有更多的数据加载了", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 刷新获取数据的方法
     */
    private void refreshData() {
        //刷新数据的时候，始终定位到第一页
        curPage = 1;

        //把状态改成刷新的状态
        STATE = STATE_REFRESH;

        //调用获取数据的方法
        requestWares(category_id);
    }

    /**
     * 加载更多
     */
    private void loadMoreData() {

        //当调用的时候，页数自动加一
        curPage = ++curPage;
        //状态改成加载更多，以方便跳转到此逻辑中
        STATE = STATE_MORE;
        requestWares(category_id);

    }

    /**
     * 请求分类商品中的分类列表的方法
     */
    private void requestCategoryData() {
        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallback<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {

                showCategoryData(categories);

                if (categories != null && categories.size() > 0) {
                    category_id = categories.get(0).getId();  //获取到分类列表的id
                    requestWares(category_id);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 展示分类列表
     *
     * @param categories
     */
    private void showCategoryData(List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(getContext(), categories);

        /**
         * 对分类项目实现按钮监听事件，以方便后继的二级商品的加载对应
         */
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                Category category = mCategoryAdapter.getItem(position);
                category_id = category.getId();
                curPage = 1;
                STATE = STATE_NORMAL;
                requestWares(category_id);
            }

        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }


    /**
     * 滑动广告请求方法
     */
    private void requestBannerData() {
        String url = Contants.API.BANNER + "?type=1";
        mHttpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderView(banners);   //把请求成功的数据传入到show方法中，来显示请求成功的数据
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 用来解析gson数据，初始化轮番广告
     */
    public void showSliderView(List<Banner> mBanners) {

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
        //mSliderLayout.setCustomIndicator(mIndicator);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        //3秒的时间
        mSliderLayout.setDuration(3000);

    }


    /**
     * 二级商品数据的请求方式
     *
     * @param categoryId
     */
    public void requestWares(long categoryId) {
        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + curPage + "&pageSize=" + pageSize;
        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
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
            public void onSuccess(Response response, Page<Wares> waresPage) {

                curPage = waresPage.getCurrentPage();   //当前页
                totalPage = waresPage.getTotalPage();   //总页数
                showWaresData(waresPage.getList());   //把获取到的商品传给show方法，来显示获取到的数据
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 展示数据，我们就会需要adapter
     */
    public void showWaresData(List<Wares> wares) {

        switch (STATE) {

            case STATE_NORMAL:    //正常状态

                wareAdapter = new WareAdapter(getContext(), wares);

                mWaresRecycleView.setAdapter(wareAdapter);

                //设置一个排版方式
                mWaresRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                //设置动画
                mWaresRecycleView.setItemAnimator(new DefaultItemAnimator());
                //设置
                mWaresRecycleView.addItemDecoration(new DividerGridItemDecoration(getContext()));

                break;

            case STATE_REFRESH:   //刷新状态
                //刷新的时候首先要清楚adapter中的数据，要不然就会加载好多重复的数据
                wareAdapter.clear();
                //然后调用添加数据的方法来添加数据
                wareAdapter.addData(wares);
                //调用这个方法，定位到第几个
                mWaresRecycleView.scrollToPosition(0);
                //完成刷新
                mRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:    //添加状态
                wareAdapter.addData(wareAdapter.getDatas().size(), wares);
                mWaresRecycleView.scrollToPosition(wareAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }


}
