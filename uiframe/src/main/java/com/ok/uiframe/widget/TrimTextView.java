package com.ok.uiframe.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * 处理textview 有多余空格问题 比如英文单词不拆分的问题
 */
public class TrimTextView extends androidx.appcompat.widget.AppCompatTextView {
    public TrimTextView(Context context) {
        super(context);
    }

    public TrimTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TrimTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 使用该方法设置TextView的文本内容,改方法不能再主线程中执行
     * @param text
     */
    public void setAdaptiveText(String text) {
        this.setText(text);
        this.post(() -> TrimTextView.this.setText(trimText(TrimTextView.this)));
    }
    
    private String trimText(TextView textView) {
        final String originalText = textView.getText().toString(); //原始文本
        final Paint tvPaint = textView.getPaint();//获取TextView的Paint
        final float tvWidth = textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight(); //TextView的可用宽度
        //将原始文本按行拆分
        String[] originalTextLines = originalText.replaceAll("\r", "").split("\n");
        StringBuilder newTextBuilder = new StringBuilder();
        for (String originalTextLine : originalTextLines) {
            //文本内容小于TextView宽度，即不换行，不作处理
            if (tvPaint.measureText(originalTextLine) <= tvWidth) {
                newTextBuilder.append(originalTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int i = 0; i != originalTextLine.length(); ++i) {
                    char charAt = originalTextLine.charAt(i);
                    lineWidth += tvPaint.measureText(String.valueOf(charAt));
                    if (lineWidth <= tvWidth) {
                        newTextBuilder.append(charAt);
                    } else {
                        //单行超过TextView可用宽度，换行
                        newTextBuilder.append("\n");
                        lineWidth = 0;
                        --i;//该代码作用是将本轮循环回滚，在新的一行重新循环判断该字符
                    }
                }
            }
        }
        return newTextBuilder.toString();
    }
}