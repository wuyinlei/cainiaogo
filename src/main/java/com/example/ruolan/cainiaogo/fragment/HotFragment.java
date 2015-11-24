package com.example.ruolan.cainiaogo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.activity.WareDetailActivity;
import com.example.ruolan.cainiaogo.adapter.BaseAdapter;
import com.example.ruolan.cainiaogo.adapter.HWAdapter;
import com.example.ruolan.cainiaogo.bean.Page;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.example.ruolan.cainiaogo.uri.Contants;
import com.example.ruolan.cainiaogo.utils.Pager;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by ruolan on 2015/11/11.
 */
public class HotFragment extends BaseFragment implements Pager.OnPageListener<Wares> {

    //创建adapter
    private HWAdapter mAdapter;

    //一个listview相似
    private RecyclerView mRecyclerView;

    //刷新的控件
    private MaterialRefreshLayout mRefreshLayout;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载自定义的布局
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        return view;
    }

    @Override
    public void init() {

        Pager pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .build(getContext(), new TypeToken<Page<Wares>>() {}.getType());


        pager.request();

    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {

        mAdapter = new HWAdapter(getContext(),datas);

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Wares wares = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Contants.WARE,wares);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.refreshData(datas);

        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {

        mAdapter.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
    }

}
