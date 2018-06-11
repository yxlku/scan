package com.longpc.scanlibrary.callback;


import com.longpc.scanlibrary.ScanEditText;

/**
 * 扫描枪打卡扫描监听
 */
public interface IScanResultCallback {


    /**
     * 签到操作
     * @param scanText 扫描结果
     * @return 签到状态
     */
    void signOperating(ScanEditText scanEditText, String scanText);

    /**
     * 签到中
     */
    void signing();

}
