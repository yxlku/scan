package com.longpc.scanlibrary;

/**
 * 签到状态
 */
public enum ScanState {
    /** 签到中*/
    SCANING,
    /** 初始化*/
    INIT,
    /** 签到完成*/
    FINISH,
    /** 签到失败*/
    FAIL;
}
