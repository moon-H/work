package com.lee.study.retrofit.mvp.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lee.study.retrofit.R;
import com.lee.study.retrofit.base.BaseActivity;
import com.lee.study.retrofit.base.utils.CssPermissionDeniedException;
import com.lee.study.retrofit.base.utils.DeviceInfo;
import com.lee.study.retrofit.base.utils.Utils;
import com.lee.study.retrofit.mvp.ILoginContract;
import com.lee.study.retrofit.mvp.presenter.LoginPresenter;

/**
 * Created by liwx on 2018/2/25.
 */

public class LoginActivity extends BaseActivity implements ILoginContract.ILoginView {
    private static final String TAG = "LoginActivity";
    private LoginPresenter mLoginPresenter;
    private EditText mEdtPhoneNumber;
    private EditText mEdtCode;
    private Button mBtnSend;
    private Button mBtnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate");
        mLoginPresenter = new LoginPresenter(this, this);
        mEdtPhoneNumber = findViewById(R.id.edt_phone);
        mEdtCode = findViewById(R.id.edt_auth_code);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnLogin = findViewById(R.id.btn_login);

        try {
            Log.d(TAG, "iccid =" + DeviceInfo.getICCID(this));
            Log.d(TAG, "MD5 =" + Utils.generatePassword("111111"));
        } catch (CssPermissionDeniedException e) {
            e.printStackTrace();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginPresenter.sendAuthCode(mEdtPhoneNumber.getText().toString().trim());
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "点击了~~~~");
                mLoginPresenter.getInboxList();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == 1) {
            //判断权限是否申请通过
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //授权成功
                Log.d(TAG, "授权成功");
            } else {
                //授权失败
                Log.d(TAG, "授权失败");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void senAuthCodeSuccess() {

    }
}
