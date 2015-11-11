package com.example.ruolan.cainiaogo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.bean.Tab;
import com.example.ruolan.cainiaogo.fragment.CartFragment;
import com.example.ruolan.cainiaogo.fragment.CategoryFragment;
import com.example.ruolan.cainiaogo.fragment.HomeFragment;
import com.example.ruolan.cainiaogo.fragment.HotFragment;
import com.example.ruolan.cainiaogo.fragment.MineFragment;
import com.example.ruolan.cainiaogo.weiget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

//FragmentTabHost的用法
//第一步：Activity继承自FragmentActivity(AppCompatActivity这个也是继承自FragmentActivity的)
public class MainActivity extends AppCompatActivity {


    private FragmentTabHost mTabHost;      //这个使用的是已经写好的一个FragmentTabHost

    private List<Tab> mTabs = new ArrayList<>(5);
    private LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
    }

    private void initTab() {
        //创建五个底部显示的tab
        Tab tab_home = new Tab(R.string.home,R.mipmap.icon_home,HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot,R.mipmap.icon_hot,HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory,R.mipmap.icon_discover,CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart,R.mipmap.icon_cart,CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine,R.mipmap.icon_user,MineFragment.class);

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

        for (Tab tab:mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            //在这里我们要加载布局，为了实现tabhost
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec,tab.getFragment(),null);
        }
    }

    private View buildIndicator(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getIcon());   //设置背景图片
        text.setText(tab.getTitle());   //设置文字
        return view;
    }




}
