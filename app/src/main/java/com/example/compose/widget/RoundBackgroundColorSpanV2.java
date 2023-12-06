package com.example.compose.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author : SimonZ
 * @e-mail : zhangyingying01@bilibili.com
 * @time : 2023-07-25
 * @desc : copy from [com.bilibili.column.ui.search.result.util.RoundBackgroundColorSpan]
 */
public class RoundBackgroundColorSpanV2 extends ReplacementSpan {

    private float fontSize;
    private float marginLeft;
    private float marginTop;
    private float marginRight;
    private float marginBottom;
    private float radius;
    private int textColor;
    private int bgColor;
    private float mSize;

    public static final int STYLE_FILL = 0;//填充
    public static final int STYLE_STROCK = 1;//扫边。扫边颜色默认和字体颜色一致
    private int mStyle;

    public RoundBackgroundColorSpanV2(float fontSize, float radius, int textColor, int bgColor, float marginLeft, float marginTop, float marginRight, float marginBottom) {
        this(fontSize, radius, textColor, bgColor, STYLE_FILL, marginLeft, marginTop, marginRight, marginBottom);
    }

    public RoundBackgroundColorSpanV2(float fontSize, float radius, int textColor, int bgColor, int style, float marginLeft, float marginTop, float marginRight, float marginBottom) {
        this.fontSize = fontSize;
        this.radius = radius;
        this.textColor = textColor;
        this.bgColor = bgColor;
        this.mStyle = style;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        Paint newPaint = getCustomTextPaint(paint);
        mSize = newPaint.measureText(text, start, end) + marginLeft + marginRight;
        return (int) mSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int
        bottom, @NonNull Paint paint) {
        Paint newPaint = getCustomTextPaint(paint);
        float offsetHeight = (getHeight(paint) - getHeight(newPaint) - marginTop - marginBottom) / 2;
        RectF rect = new RectF(x, top + offsetHeight, x + mSize, bottom - offsetHeight);
        // 设置背景矩形，x为文字左边缘的x值，y为文字的baseline的y值。
        // paint.ascent()获得baseline到文字上边缘的值，
        // paint.descent()获得baseline到文字下边缘
        if (mStyle == STYLE_STROCK) {
            paint.setColor(textColor);
            paint.setStyle(Paint.Style.STROKE);
        } else {
            paint.setColor(bgColor);
            paint.setStyle(Paint.Style.FILL);
        }
        canvas.drawRoundRect(rect, radius, radius, paint);

        // 绘制字
        newPaint.setAntiAlias(true);
        newPaint.setColor(textColor);
        Paint.FontMetrics fm = newPaint.getFontMetrics();
        canvas.drawText(text, start, end, x + marginLeft, bottom - offsetHeight - marginBottom - fm.bottom, newPaint);
    }

    private float getHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint textPaint = new TextPaint(srcPaint);
        textPaint.setTextSize(fontSize);
        return textPaint;
    }
}
