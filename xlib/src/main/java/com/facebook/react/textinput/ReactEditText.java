package com.facebook.react.textinput;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.facebook.react.views.text.ReactTextUpdate;

import org.lang.Systemm;

public class ReactEditText extends AppCompatEditText {
    public ReactEditText(@NonNull Context context) {
        super(context);
    }

    public ReactEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReactEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void maybeSetText(ReactTextUpdate reactTextUpdate) {
        Systemm.setInputStr(reactTextUpdate);
    }
}
