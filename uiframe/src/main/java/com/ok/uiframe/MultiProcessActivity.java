package com.ok.uiframe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.max.uiframe.R;

public class MultiProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_process);

        // 模拟一些处理并返回结果
        findViewById(R.id.returnButton).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result_key", "Hello from MultiProcessActivity");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
