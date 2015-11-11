package com.example.ruolan.cainiaogo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.fragment.HomeFragment;
import com.example.ruolan.cainiaogo.weiget.FragmentTabHost;

//FragmentTabHost的用法
//第一步：Activity继承自FragmentActivity(AppCompatActivity这个也是继承自FragmentActivity的)
public class MainActivity extends AppCompatActivity {


    private FragmentTabHost mTabHost;      //这个使用的是已经写好的一个FragmentTabHost

    private LayoutInflater mInflater;      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInflater = LayoutInflater.from(this);
        //找到tabhost
        mTabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        //第二步：调用setup()方法
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        //第三步：添加TabSpec
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("home");
        //在这里我们要加载布局，为了实现tabhost
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(R.mipmap.icon_home);
        text.setText("主页");
        tabSpec.setIndicator(view);   //把布局加载到tabspec中
        mTabHost.addTab(tabSpec, HomeFragment.class,null);    //然后实现它

    }
}
