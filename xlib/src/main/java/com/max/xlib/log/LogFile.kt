package com.max.xlib.log;

import android.util.Log;

public class LogHelper {
    private static String TAG = "xlib";

    public static void log(String message){
        Log.d(TAG, message);
    }

}
