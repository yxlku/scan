package com.longpc.scanlibrary;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.longpc.scanlibrary.callback.IScanResultCallback;
import com.longpc.scanlibrary.callback.IScanScanCallback;

/**
 * 打卡签到
 */
public class ScanEditText extends AppCompatEditText implements TextView.OnEditorActionListener {
    public static final String TAG = "TAG_QDEditText";
    /** mini模式 */
    public static boolean MINI = false;
    /** 签到状态*/
    private ScanState scanState = ScanState.INIT;
    /** 键盘按键监听*/
    private IScanScanCallback iScanScanCallback;
    /** 扫描结果监听*/
    private IScanResultCallback iScanResultCallback;

    public ScanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!MINI) {
            int widthSize = 1;
            int heightSize = 1;
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    private void init() {
        //不弹出键盘
        setInputType(InputType.TYPE_NULL);
        //回车监听
        setOnEditorActionListener(this);
    }

    /**
     * 设置签到状态
     * @param scanState
     */
    public void setScanState(ScanState scanState) {
        this.scanState = scanState;
    }

    public void setIQDScanListener(IScanScanCallback iScanScanCallback) {
        this.iScanScanCallback = iScanScanCallback;
    }

    /**
     * 签到状态监听
     * @param iScanResultCallback
     */
    public void setIScanResultListener(IScanResultCallback iScanResultCallback) {
        this.iScanResultCallback = iScanResultCallback;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
            //键盘回车键
            if (iScanScanCallback != null) {
                iScanScanCallback.success(v);
            }
            //扫描结果处理
            if (iScanResultCallback != null) {
                //扫描结果
                String scanText = v.getText().toString().trim();
                //0、清空输入框中的内容
                setHint(scanText);
                setText("");
                Log.e(TAG, "state : " + scanState);
                if (scanState != ScanState.SCANING) {
                    //可以签到
                    //1、将状态设置为签到中
                    scanState = ScanState.SCANING;
                    //2、签到具体操作 -- 返回签到状态
                    if (!TextUtils.isEmpty(scanText)) {
                        iScanResultCallback.signOperating(this, scanText);
                    }
                } else {
                    //签到中，不可以签到
                    Log.e(TAG, "签到中...");
                    iScanResultCallback.signing();
                }
            }
        }
        return false;
    }

}
