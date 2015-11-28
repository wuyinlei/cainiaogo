package com.example.ruolan.cainiaogo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.Tab;
import com.example.ruolan.cainiaogo.fragment.CartFragment;
import com.example.ruolan.cainiaogo.fragment.CategoryFragment;
import com.example.ruolan.cainiaogo.fragment.HomeFragment;
import com.example.ruolan.cainiaogo.fragment.HotFragment;
import com.example.ruolan.cainiaogo.fragment.MineFragment;
import com.example.ruolan.cainiaogo.weiget.CnToolbar;
import com.example.ruolan.cainiaogo.weiget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

//FragmentTabHost的用法
//第一步：Activity继承自FragmentActivity(AppCompatActivity这个也是继承自FragmentActivity的)
public class MainActivity extends BaseActivity {


    private FragmentTabHost mTabHost;      //这个使用的是已经写好的一个FragmentTabHost

    private CnToolbar mToolbar;

    Fragment fragment;
    CartFragment cartFragment;

    //用list数组来存贮tab
    private List<Tab> mTabs = new ArrayList<>(5);
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_test);
        initToolBar();
        initTab();
    }

    private void initToolBar() {
        mToolbar = (CnToolbar) findViewById(R.id.toolbar);
    }

    /**
     * 初始化底部tabhost
     */
    private void initTab() {
        //创建五个底部显示的tab
        Tab tab_home = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        final Tab tab_cart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        final Tab tab_mine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);

        //把创建的tab添加到list数组中
        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        //第二步：调用setup()方法
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            //在这里我们要加载布局，为了实现tabhost
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == getString(R.string.cart)) {
                    refData();
                }else if (tabId == getString(R.string.mine)){
                    mToolbar.hideSearchView();
                }
                else {
                    mToolbar.hideTitleView();
                    mToolbar.showSearchView();
                    mToolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });

        //去掉底部的分割线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);

    }

    public void refData(){
        if (cartFragment == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refData();
                cartFragment.changeToolbar();
            }
        }
        else {
            cartFragment.refData();
        }
    }


    /**
     * 创建view布局
     *
     * @param tab
     * @return
     */
    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        //初始化布局控件
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getIcon());   //设置背景图片
        text.setText(tab.getTitle());   //设置文字
        return view;
    }


}
