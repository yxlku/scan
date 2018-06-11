package com.longpc.sacn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.longpc.scanlibrary.ScanEditText;
import com.longpc.scanlibrary.ScanState;
import com.longpc.scanlibrary.callback.IScanResultCallback;
import com.longpc.scanlibrary.util.ScanToastUtil;
import com.longpc.scanlibrary.util.ScanVoicePlay;

public class MainActivity extends AppCompatActivity {

    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScanEditText mScanEditText = findViewById(R.id.mScanEditText);

        //扫码枪结果监听
        mScanEditText.setIScanResultListener(new IScanResultCallback() {
            @Override
            public void signOperating(final ScanEditText signEditText, String scanText) {
                //扫码成功后的操作 -- 这里模拟请求操作
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScanToastUtil.showToast(MainActivity.this, "签到成功");
                        //签到请求成功后播放音乐
                        ScanVoicePlay.with(MainActivity.this).play("qd.mp3");
                        //设置签到成功后的状态
                        signEditText.setScanState(ScanState.FINISH);
                    }
                }, 3000);
            }

            @Override
            public void signing() {
                //签到中
                ScanToastUtil.showToast(MainActivity.this, "签到中。。。。");
            }
        });
    }
}
