package com.longpc.scanlibrary.callback;

import android.widget.TextView;

/**
 * 扫描枪打卡扫描监听
 */
public interface IScanScanCallback {

    /**
     * 扫描完成
     */
    void success(TextView v);

}
