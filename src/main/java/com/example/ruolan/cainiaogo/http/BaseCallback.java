package com.example.ruolan.cainiaogo.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ruolan on 2015/11/12.
 */
public abstract class BaseCallback<T> {

    public Type mType;

    /**
     * 下面这几行是为了把T这个类型转换成Type这个类型所做的一些事情
     */
    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback()
    {
        mType = getSuperclassTypeParameter(getClass());
    }


    /**
     * 在请求之前调用
     * @param request
     */
    public  abstract void onBeforeRequest(Request request);


    /**
     * 请求失败的时候调用
     * @param request
     * @param e
     */
    public abstract  void onFailure(Request request, Exception e) ;


    /**
     *请求成功时调用此方法
     * @param response
     */
    public abstract  void onResponse(Response response);

    /**
     *
     * 状态码大于200，小于300 时调用此方法
     * @param response
     * @param t
     * @throws IOException
     */
    public abstract void onSuccess(Response response,T t) ;

    /**
     * 状态码400，404，403，500等时调用此方法
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code,Exception e) ;
}
