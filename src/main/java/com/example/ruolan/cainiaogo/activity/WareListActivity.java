package com.example.ruolan.cainiaogo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.adapter.Decoration.DividerItemDecoration;
import com.example.ruolan.cainiaogo.adapter.HWAdapter;
import com.example.ruolan.cainiaogo.bean.Page;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.example.ruolan.cainiaogo.uri.Contants;
import com.example.ruolan.cainiaogo.utils.Pager;
import com.example.ruolan.cainiaogo.weiget.CnToolbar;
import com.google.gson.reflect.TypeToken;

import java.util.List;


/**
 * Created by ruolan on 2015/11/19.
 */
public class WareListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares>, TabLayout.OnTabSelectedListener ,View.OnClickListener{

    private static final String TAG = "WareListActivity";


    private CnToolbar mToolbar;

    private TabLayout mTabLayout;

    public Pager pager;

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;

    private TextView mTextSummary;

    private RecyclerView mRecyclerView;

    private MaterialRefreshLayout mRefreshLayout;

    private int orderBy = 0;

    private long campaignId = 0;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GRID = 2;


    private HWAdapter mWaresAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wareist);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTextSummary = (TextView) findViewById(R.id.text_summary);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_layout);

        campaignId = getIntent().getLongExtra(Contants.COMPAINGAIN_ID, 0);

        mToolbar = (CnToolbar) findViewById(R.id.toolbar);

        initTab();
        getData();
        initToolBar();
    }

    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);


        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setTag(TAG_SALE);
        tab.setText("销量");
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);
    }

    private void initToolBar() {
        mToolbar.setNavigationIcon(R.drawable.icon_back_32px);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });
        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);
        mToolbar.setRightButtonOnClickListener(this);

    }

    private void getData() {
        pager = Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId", campaignId)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Wares>>() {
                }.getType());
        pager.request();
    }


    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mTextSummary.setText("共有" + totalCount + "件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HWAdapter(this, datas);
            mRecyclerView.setAdapter(mWaresAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (ACTION_LIST == action){
            mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            mWaresAdapter.resetLayout(R.layout.template_grid_wares);
            mToolbar.getRightButton().setTag(ACTION_GRID);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        else if (ACTION_GRID == action){
            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
            mWaresAdapter.resetLayout(R.layout.template_hot_wares);
            mToolbar.getRightButton().setTag(ACTION_LIST);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }
}
