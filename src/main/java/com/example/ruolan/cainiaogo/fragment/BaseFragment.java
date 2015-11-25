package com.example.ruolan.cainiaogo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ruolan.cainiaogo.activity.LoginActivity;
import com.example.ruolan.cainiaogo.application.CniaoApplication;
import com.example.ruolan.cainiaogo.bean.User;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by ruolan on 2015/11/17.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = createView(inflater,container,savedInstanceState);
        ViewUtils.inject(this, view);

        initToolBar();
        init();
        return view;

    }

    public void  initToolBar(){

    }


    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();

    public void startActivity(Intent intent, boolean isNeedLogin) {

        if (isNeedLogin) {   //如果有登录的意图
            User user = CniaoApplication.getInstance().getUser();
            if (user != null) { //说明已经登录了，就调转到目标activity
                super.startActivity(intent);
            } else {
                //如果没有登录，那么我们就跳转到登录的activity
                CniaoApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }

    }

}
