package com.ok.uiframe;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MultiProcessActivityLauncher {

    private final ActivityResultLauncher<Intent> activityResultLauncher;
    private final AppCompatActivity activity;

    public interface ResultCallback {
        void onResult(String result);
    }

    public MultiProcessActivityLauncher(AppCompatActivity activity, ResultCallback callback) {
        this.activity = activity;
        activityResultLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String resultString = data.getStringExtra("result_key");
                            if (callback != null) {
                                callback.onResult(resultString);
                            }
                        }
                    }
                }
        );
    }

    public void launch() {
        Intent intent = new Intent(activity, MultiProcessActivity.class);
        activityResultLauncher.launch(intent);
    }
}
