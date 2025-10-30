package com.ok.uiframe;

/**
 * Created by admin on 2018/4/9.
 */

public interface OnPermissionRequestCallBack {

    /**
     * 请求成功
     */
    void requestSuccess();

    /**
     * 请求失败
     */
    void requestFailed(boolean isShowRequest);

}
