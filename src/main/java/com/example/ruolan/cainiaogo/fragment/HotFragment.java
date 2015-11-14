package com.example.ruolan.cainiaogo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.adapter.Decoration.DividerItemDecoration;
import com.example.ruolan.cainiaogo.adapter.HWAdapter;
import com.example.ruolan.cainiaogo.bean.Page;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.example.ruolan.cainiaogo.http.OkHttpHelper;
import com.example.ruolan.cainiaogo.http.SpotsCallback;
import com.example.ruolan.cainiaogo.uri.Contants;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * Created by ruolan on 2015/11/11.
 */
public class HotFragment extends Fragment {

    //当前页数
    private int curPage = 1;

    //每页最大的数量
    private int pageSize = 10;

    //总的页数
    private int totalPage;

    //创建一个wares的list的数据数组
    private List<Wares> datas;

    //创建adapter
    private HWAdapter mAdapter;

    //一个listview相似
    private RecyclerView mRecyclerView;

    //刷新的控件
    private MaterialRefreshLayout mRefreshLayout;


    //定义三种状态，分别是刷新之前，刷新中，加载更多
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int STATE = STATE_NORMAL;

    //用到的http方法
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //加载自定义的布局
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        initRefreshLayout();

        getData();

        return view;
    }

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
                String x = datas.toString();
                refreshData();
                String y = datas.toString();
                //判断下拉刷新前后是否是同一个数据，如果是同一个数据，提示不需要刷新了
                if (x.equals(y)) {
                    Toast.makeText(getActivity(), "已经是最新的数据了，不能再加载了", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * 上拉加载更多的方法
             * @param materialRefreshLayout
             */
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (curPage <= totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "已经是最后一页了，没有更多的数据加载了", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 取数据的方法
     */
    public void getData() {

        String url = Contants.API.WARES_HOT + "?" + "curPage=" + curPage + "&" + "pageSize=" + pageSize;
        mHttpHelper.get(url, new SpotsCallback<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                datas = waresPage.getList();
                //找到当前的页数
                curPage = waresPage.getCurrentPage();

                totalPage = waresPage.getTotalPage();
                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 展示数据，我们就会需要adapter
     */
    public void showData() {

        switch (STATE) {

            case STATE_NORMAL:    //正常状态

                mAdapter = new HWAdapter(getContext(),datas);
                /*mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Log.d("wuyinlei",position + "");
                    }
                });*/
                mRecyclerView.setAdapter(mAdapter);

                //设置一个排版方式
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                //设置动画
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                //设置
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

               /* mAdapter = new HotWaresAdapter(datas);

                //把adapter加入到recycleview中
                mRecyclerView.setAdapter(mAdapter);

                //设置一个排版方式
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                //设置动画
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                //设置
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                */
               /* mRecyclerView.setAdapter(new BaseAdapter<Wares,BaseViewHolder>(getContext(), datas, R.layout.template_hot_wares) {

                    @Override
                    public void bindData(BaseViewHolder viewHolder, Wares wares) {
                        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
                        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

                        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
                        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice().shortValue());
                    }
                });*/

                break;

            case STATE_REFRESH:   //刷新状态
                //刷新的时候首先要清楚adapter中的数据，要不然就会加载好多重复的数据
                mAdapter.clear();
                //然后调用添加数据的方法来添加数据
                mAdapter.addData(datas);
                //调用这个方法，定位到第几个
                mRecyclerView.scrollToPosition(0);
                //完成刷新
                mRefreshLayout.finishRefresh();
                break;


            case STATE_MORE:    //添加状态
                mAdapter.addData(mAdapter.getDatas().size(), datas);
                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

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
        getData();
    }

    private void loadMoreData() {
        //页数定位到下一页
        curPage = ++curPage;

        //状态更改为加载更多
        STATE = STATE_MORE;
        getData();
    }

}
