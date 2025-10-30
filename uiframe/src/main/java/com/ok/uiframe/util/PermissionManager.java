package com.ok.uiframe.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

/**
 * Created by admin on 2018/3/6.
 */

public class PermissionManager {

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查多个权限权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(Activity context, String[] permissions, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(context,
                Manifest.permission.WRITE_CONTACTS)) {
            ActivityCompat.requestPermissions(context, permissions, requestCode);
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(context, permissions, requestCode);
        }
    }

    public static boolean isShowRequest(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return false;
            }
        }
        return true;
    }

}
