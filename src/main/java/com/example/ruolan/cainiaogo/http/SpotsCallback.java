package com.example.ruolan.cainiaogo.http;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import dmax.dialog.SpotsDialog;

/**
 * Created by ruolan on 2015/11/12.
 */

/**
 * github上面的一个开源的项目
 * 项目地址：https://github.com/d-max/spots-dialog
 *
 * @param <T>
 */
public abstract class SpotsCallback<T> extends  BaseCallback<T> {

    //显示dialog
    public void showDialog(){
        dialog.show();
    }

    //消失dialog
    public void dismissDialog(){
        if (dialog != null)
            dialog.dismiss();
    }

    SpotsDialog dialog ;
    //constructor
    public SpotsCallback(Context context){
        dialog = new SpotsDialog(context);
    }

    public void setMessage(String message){
        dialog.setMessage(message);
    }

    @Override
    public void onBeforeRequest(Request request) {
        //在请求之前显示dialog，以提示用户正在加载
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        //在请求不成功的时候把dialog给消除
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        //在请求成功的时候也要把dialog给消除
        dismissDialog();
    }


}
