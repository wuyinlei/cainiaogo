package com.example.ruolan.cainiaogo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.utils.ToastUtils;
import com.example.ruolan.cainiaogo.weiget.ClearEditText;
import com.example.ruolan.cainiaogo.weiget.CnToolbar;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;

public class RegActivity extends AppCompatActivity {


    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    private CnToolbar mToolbar;

    private TextView mTextCountry;

    private TextView mTextCountryCode;

    private ClearEditText mEditPhone;

    private ClearEditText mEditPwd;

    private SpotsDialog dialog;

    private SMSEvenHandler evenHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //初始化各种控件
        mToolbar = (CnToolbar) findViewById(R.id.toolbar);
        mTextCountry = (TextView) findViewById(R.id.textCountry);
        mTextCountryCode = (TextView) findViewById(R.id.textCountryCode);
        mEditPhone = (ClearEditText) findViewById(R.id.edit_phone);
        mEditPwd = (ClearEditText) findViewById(R.id.edit_pwd);

        initToolBar();

        SMSSDK.initSDK(this, "cc8b80d45336", "fdbedc0c9bcacbadb672e39927482fc9");


        evenHandler = new SMSEvenHandler();
        SMSSDK.registerEventHandler(evenHandler);

        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {
            Log.d("RegActivity", country[0] + "=" + country[1]);
            mTextCountryCode.setText("+" + country[1]);
            mTextCountry.setText(country[0]);
        }

    }

    //创建一个EventHandler的内部类
    class SMSEvenHandler extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    /**
                     * 当result=SMSSDK.RESULT_COMPLETE，则data的类型如下表所示。onUnregister在被反注册的时候被触发。
                     *
                     *       EVENT                                 DATA类型                               说明
                     * EVENT_GET_SUPPORTED_COUNTRIES	   ArrayList<HashMap<String,Object>>	返回支持发送验证码的国家列表
                     * EVENT_GET_VERIFICATION_CODE	          Boolean	                        true为智能验证，false为普通下发短信
                     * EVENT_SUBMIT_VERIFICATION_CODE	  HashMap<String,Object>	            校验验证码，返回校验的手机和国家代码
                     * EVENT_GET_CONTACTS	              ArrayList<HashMap<String,Object>>	    获取手机内部的通信录列表
                     * EVENT_SUBMIT_USER_INFO	                null	                        提交应用内的用户资料
                     * EVENT_GET_FRIENDS_IN_APP	         ArrayList<HashMap<String,Object>>	    获取手机通信录在当前应用内的用户列表
                     * EVENT_GET_VOICE_VERIFICATION_CODE	    null	                        请求发送语音验证码，无返回
                     */
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //提交验证码成功
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //请求验证码后，调转到验证码填写页面
                            // 获取验证码成功
                            afterVerificationCodeRequested((Boolean) data);
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        }
                    } else {
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
                                && data != null
                                && (data instanceof UserInterruptException)) {
                            // 由于此处是开发者自己决定要中断发送的，因此什么都不用做
                            return;
                        }
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(RegActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }

    /**
     * 请求验证码后，跳转到验证码填写页面
     */
    private void afterVerificationCodeRequested(boolean smart) {
        Toast.makeText(this, "获取验证码成功", Toast.LENGTH_SHORT).show();
    }

    private void initToolBar() {
        mToolbar.setRightButtonText(R.string.next_text);
        //对ToolBar右侧的按钮实行监听事件
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
                //SMSSDK.getVerificationCode();
            }
        });
    }


    /**
     * 判断输入的时候是手机号，或者是没有输入
     * @param phone
     * @param code
     */
    private void checkPhoneNum(String phone, String code) {
        //去掉前面的+号
        if (code.startsWith("+")) {
            code = code.substring(1);
        }
        //如果是空的，也就是说没有输入手机号码
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }
        if (code == "86") {
            if (phone.length() != 11) {
                ToastUtils.show(this, "手机号码长度不对");
                return;
            }
        }

        /**
         * 下面的是个正则表达式，这里来判断输入的时候是手机号码
         * 因为是全世界的手机号码
         * //"^1代表第1位为数字1，"(3|5|7|8|4)"代表第二位可以为3、5、7、8、4中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
         */
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtils.show(this, "您输入的手机号码格式不正确");
            return;
        }
    }

    //得到输入的手机号和密码
    private void getCode() {
        String phone = mEditPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTextCountryCode.getText().toString().trim();
        String pwd = mEditPwd.getText().toString().trim();

        //先来判断输入的时候号码和国家编号是否正确
        checkPhoneNum(phone, code);
        //not 86    +86
        //如果上一步通过，则调用sdk的getVerificationCode()方法进行验证
        SMSSDK.getVerificationCode(code, phone);
    }


    /**
     * 通过sim卡获取国家编号
     *
     * @return
     */
    private String[] getCurrentCountry() {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if (country == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country;
    }

    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }

        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHandler);
    }
}
