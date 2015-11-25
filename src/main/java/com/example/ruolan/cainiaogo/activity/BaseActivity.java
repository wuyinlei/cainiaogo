package com.example.ruolan.cainiaogo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.ruolan.cainiaogo.application.CniaoApplication;
import com.example.ruolan.cainiaogo.bean.User;

/**
 * Created by ruolan on 2015/11/25.
 */
public class BaseActivity extends AppCompatActivity {

    public void startActivity(Intent intent, boolean isNeedLogin) {

        if (isNeedLogin) {   //如果有登录的意图
            User user = CniaoApplication.getInstance().getUser();
            if (user != null) { //说明已经登录了，就调转到目标activity
                super.startActivity(intent);
            } else {
                //如果没有登录，那么我们就跳转到登录的activity
                CniaoApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }

    }
}
