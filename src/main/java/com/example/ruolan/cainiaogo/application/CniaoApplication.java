package com.example.ruolan.cainiaogo.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.ruolan.cainiaogo.bean.User;
import com.example.ruolan.cainiaogo.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by ruolan on 2015/11/12.
 */
public class CniaoApplication extends Application {
    private User user;



    private static  CniaoApplication mInstance;


    public static  CniaoApplication getInstance(){

        return  mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initUser();
        Fresco.initialize(this);
    }




    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }


    public User getUser(){

        return user;
    }


    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }



    private  Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }

}
