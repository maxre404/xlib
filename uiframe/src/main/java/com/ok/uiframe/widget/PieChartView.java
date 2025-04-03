package com.ok.uiframe.widget;

import android.content.Context;
import android.graphics.*;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
    private Paint paintFill;
    private TextPaint paintText;
    private int[] colors = {
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.CYAN, Color.MAGENTA, Color.DKGRAY, Color.LTGRAY
    };
    private String[] dataList = {
            "苹果", "橘子", "香蕉", "葡萄",
            "西瓜", "芒果", "这是一个超长的水果名字示例", "这个名字真的很长很长",
    };

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFill.setStyle(Paint.Style.FILL);

        paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(40f);
        paintText.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = getWidth() / 3f;
        float textRadius = radius * 0.7f;  // 文字绘制的半径位置

        float sweepAngle = 360f / dataList.length;
        float startAngle = 0f;

        RectF rectF = new RectF(
                centerX - radius, centerY - radius,
                centerX + radius, centerY + radius
        );

        for (int i = 0; i < dataList.length; i++) {
            // 设置扇形颜色
            paintFill.setColor(colors[i % colors.length]);
            // 绘制扇形
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paintFill);

            // 计算扇形中心点
            double angleRad = Math.toRadians(startAngle + sweepAngle / 2);
            float textCenterX = centerX + textRadius * (float) Math.cos(angleRad);
            float textCenterY = centerY + textRadius * (float) Math.sin(angleRad);

            // 计算文本最大宽度
            int textMaxWidth = (int) (radius * 0.5f);

            // **使用 StaticLayout 控制最大 2 行 + 省略号**
            StaticLayout staticLayout = StaticLayout.Builder.obtain(dataList[i], 0, dataList[i].length(), paintText, textMaxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_CENTER)
                    .setMaxLines(2)  // 限制最多 2 行
                    .setEllipsize(TextUtils.TruncateAt.END)  // 超过 2 行时才省略
                    .setLineSpacing(0f, 1f)
                    .setIncludePad(false)
                    .build();

            // 计算文本高度
            float textHeight = staticLayout.getHeight();

            // 计算起始位置，使文本居中
            float textStartX = textCenterX - textMaxWidth / 2f;
            float textStartY = textCenterY - textHeight / 2f;

            canvas.save();
            // 旋转文本，使其与扇形对齐
            canvas.rotate(startAngle + sweepAngle / 2, textCenterX, textCenterY);
            canvas.translate(textStartX, textStartY);
            staticLayout.draw(canvas);
            canvas.restore();

            // 更新起始角度
            startAngle += sweepAngle;
        }
    }
}
