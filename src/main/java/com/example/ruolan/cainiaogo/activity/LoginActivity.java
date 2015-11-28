package com.example.ruolan.cainiaogo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ruolan.cainiaogo.verification.Code;
import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.application.CniaoApplication;
import com.example.ruolan.cainiaogo.bean.User;
import com.example.ruolan.cainiaogo.http.OkHttpHelper;
import com.example.ruolan.cainiaogo.http.SpotsCallback;
import com.example.ruolan.cainiaogo.msg.LoginRespMsg;
import com.example.ruolan.cainiaogo.uri.Contants;
import com.example.ruolan.cainiaogo.utils.DESUtil;
import com.example.ruolan.cainiaogo.utils.ToastUtils;
import com.example.ruolan.cainiaogo.weiget.ClearEditText;
import com.example.ruolan.cainiaogo.weiget.CnToolbar;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruolan on 2015/11/23.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private CnToolbar mToolbar;

    private ClearEditText mEditPhone;

    private ClearEditText mEditPwd;

    private Button mRefresh;

    private ImageView iv_img;
    private EditText iv_edit;
    private Button btn_login;
    String getCode = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (CnToolbar) findViewById(R.id.toolbar);

        mEditPhone = (ClearEditText) findViewById(R.id.edit_phone);

        mEditPwd = (ClearEditText) findViewById(R.id.edit_pwd);

        mRefresh = (Button) findViewById(R.id.refresh);
        mRefresh.setOnClickListener(this);
        iv_edit = (EditText) findViewById(R.id.vc_code);

        iv_img = (ImageView) findViewById(R.id.vc_image);
        iv_img.setImageBitmap(Code.getInstance().getBitmap());
        getCode = Code.getInstance().getCode();

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        initToolBar();
    }

    private void initToolBar() {
        //mToolbar.hideSearchView();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                iv_img.setImageBitmap(Code.getInstance().getBitmap());
                //getCode = Code.getInstance().getCode();
                break;
            case R.id.btn_login:

                String phone = mEditPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(LoginActivity.this, "请输入手机号");
                    return;
                }

                String pwd = mEditPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.show(LoginActivity.this, "请输入密码");
                    return;
                }

                final String v_code = iv_edit.getText().toString().trim();
                String url = Contants.API.LOGIN;
                Map<String, String> params = new HashMap<>(2);
                params.put("phone", phone);
                params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));
                mHttpHelper.post(url, params, new SpotsCallback<LoginRespMsg<User>>(this) {


                    @Override
                    public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                        CniaoApplication application = CniaoApplication.getInstance();
                        application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                        /**
                         * 添加验证码验证
                         */
                        if (v_code == null || v_code.equals("")) {
                            Toast.makeText(LoginActivity.this, "没有填写验证码", Toast.LENGTH_SHORT).show();
                        } else if (!v_code.equals(getCode)) {
                            Toast.makeText(LoginActivity.this, "验证码填写不正确", Toast.LENGTH_SHORT).show();
                        } else if (application.getIntent() == null) {
                            setResult(RESULT_OK);
                            LoginActivity.this.finish();
                        } else {
                            application.jumpToTargetActivity(LoginActivity.this);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
                break;
            default:
                break;
        }
    }
}
