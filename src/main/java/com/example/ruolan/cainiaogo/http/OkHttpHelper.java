package com.example.ruolan.cainiaogo.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruolan on 2015/11/12.
 */
public class OkHttpHelper {


    public static final String TAG = "OkHttpHelper";

    //这里使用单例模式，静态私有化
    private static OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;

    private Handler mHandler;


    //初始化OkHttpHelper初值
    static {
        mInstance = new OkHttpHelper();
    }

    //构造函数私有化
    private OkHttpHelper() {

        //对mHttpClient的一些简单的设置
        mHttpClient = new OkHttpClient();
        //连接主机超时
        mHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        //从主机读取数据超时
        mHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        //写出数据超时
        mHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);

        mGson = new Gson();

        mHandler = new Handler(Looper.getMainLooper());

    }


    /**
     * 对外提供一个公开的静态的实例方法
     *
     * @return
     */
    public static OkHttpHelper getInstance() {
        return mInstance;
    }


    /**
     * Get方式
     *
     * @param url
     * @param callback
     */
    public void get(String url, BaseCallback callback) {


        Request request = buildGetRequest(url);

        request(request, callback);

    }


    /**
     * Post方式
     *
     * @param url
     * @param param
     * @param callback
     */
    public void post(String url, Map<String, String> param, BaseCallback callback) {

        Request request = buildPostRequest(url, param);
        request(request, callback);
    }


    /**
     * request请求，在这里实现了自定义的calllback方法
     *
     * @param request
     * @param callback
     */
    public void request(final Request request, final BaseCallback callback) {

        callback.onBeforeRequest(request);

        mHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(request, e);

            }
            @Override
            public void onResponse(Response response) throws IOException {

                //不管成功失败都调用
                callback.onResponse(response);

                if (response.isSuccessful()) {

                    String resultStr = response.body().string();

                    // Log.d(TAG, "result=" + resultStr);

                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object obj = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, obj);
                        } catch (com.google.gson.JsonParseException e) { // Json解析的错误
                            callback.onError(response, response.code(), e);
                        }
                    }
                } else {
                    callbackError(callback, response, null);
                }

            }
        });


    }


    /**
     * @param callback
     * @param response
     * @param obj
     */
    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {

        /**
         * 用了handler异步方法，要不然就会报错的
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }


    /**
     * 请求失败的时候调用
     *
     * @param callback
     * @param response
     * @param e
     */
    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }


    private Request buildPostRequest(String url, Map<String, String> params) {

        return buildRequest(url, HttpMethodType.POST, params);
    }

    private Request buildGetRequest(String url) {

        return buildRequest(url, HttpMethodType.GET, null);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, String> params) {


        Request.Builder builder = new Request.Builder()
                .url(url);

        if (methodType == HttpMethodType.POST) {
            RequestBody body = builderFormData(params);
            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {
            builder.get();
        }


        return builder.build();
    }


    private RequestBody builderFormData(Map<String, String> params) {


        FormEncodingBuilder builder = new FormEncodingBuilder();

        if (params != null) {

            for (Map.Entry<String, String> entry : params.entrySet()) {

                builder.add(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();

    }


    /**
     * 枚举的两个类型，一个GET，一个POST
     */
    enum HttpMethodType {

        GET,
        POST,

    }

}
