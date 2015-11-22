package com.example.ruolan.cainiaogo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.example.ruolan.cainiaogo.uri.Contants;
import com.example.ruolan.cainiaogo.utils.CartProvider;
import com.example.ruolan.cainiaogo.utils.ToastUtils;
import com.example.ruolan.cainiaogo.weiget.CnToolbar;

import java.io.Serializable;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

/**
 * Created by ruolan on 2015/11/22.
 */
public class WareDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private CnToolbar mToolbar;

    private WebView mWebView;

    private Wares mWares;

    private WebAppInterface mInterface;

    private CartProvider mCartProvider;

    private SpotsDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);

        mToolbar = (CnToolbar) findViewById(R.id.toolbar);
        mWebView = (WebView) findViewById(R.id.webView);

        //首先在进入到这的时候，拿到ware这个对象
        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if (serializable == null) {
            this.finish();
        }
        mWares = (Wares) serializable;

        mDialog = new SpotsDialog(this, "loading...");
        mDialog.show();

        mCartProvider = new CartProvider(this);
        initToolbar();
        initWebView();
    }

    private void initToolbar() {
        mToolbar.setRightButtonOnClickListener(this);
        mToolbar.setRightButtonText("分享");
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWares.getName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            oks.setImageUrl(mWares.getImgUrl());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mWares.getName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.baidu.com");

// 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ShareSDK.stopSDK();
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);

        //调用加载商品的详情页面
        mWebView.loadUrl(Contants.API.WARES_DETAIL);
        mInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mInterface, "appInterface");

        //下面的这个是
        mWebView.setWebViewClient(new WareShow());

    }


    @Override
    public void onClick(View v) {
        this.finish();
    }

    class WareShow extends WebViewClient {

        //下面的这个方法是查看页面是否加载完成，如果加载完成
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //加载对话框消失
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            //调用加载详情
            mInterface.showDetail();
        }
    }

    class WebAppInterface {

        private Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        //JS调用AS
        @JavascriptInterface
        public void showDetail() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //去调用javascript上的一个方法，前台写好的一个方法
                    mWebView.loadUrl("javascript:showDetail(" + mWares.getId() + ")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id) {

            mCartProvider.put(mWares);
            ToastUtils.show(mContext, "已经添加到购物车");
        }

        @JavascriptInterface
        public void addFacorites(long id) {

        }
    }
}
