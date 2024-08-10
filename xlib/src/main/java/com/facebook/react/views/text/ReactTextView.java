package com.facebook.react.views.text;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import org.lang.Systemm;

public class ReactTextView extends AppCompatTextView {
    public ReactTextView(@NonNull Context context) {
        super(context);
    }

    public ReactTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReactTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(ReactTextUpdate reactTextUpdate) {
        Systemm.setText(reactTextUpdate);
    }
}
