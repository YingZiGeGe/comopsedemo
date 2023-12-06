package com.example.compose.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.text.style.ReplacementSpan

/**
 *
 * @author : SimonZ
 * @e-mail : zhangyingying01@bilibili.com
 * @time   : 2023-10-18
 * @desc   : 圆角背景
 * @param fontSize 字体大小, sp2px
 * @param radius 背景圆角, dp2px
 * @param textColor 字体颜色
 * @param bgColor 背景颜色
 * @param marginLeft 边距
 *
 */
class RoundBackgroundColorSpan(
    private val fontSize: Float,
    private val radius: Float,
    private val textColor: Int,
    private val bgColor: Int,
    private val mStyle: Int,
    private val marginLeft: Float,
    private val marginTop: Float,
    private val marginRight: Float,
    private val marginBottom: Float
) : ReplacementSpan() {
    private var mSize = 0f

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val newPaint: Paint = getCustomTextPaint(paint)
        mSize = newPaint.measureText(text, start, end) + marginLeft + marginRight
        return mSize.toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val newPaint: Paint = getCustomTextPaint(paint)
        val offsetHeight = (getHeight(paint) - getHeight(newPaint) - marginTop - marginBottom) / 2
        val rect = RectF(x, top + offsetHeight, x + mSize, bottom - offsetHeight)
        // 设置背景矩形，x为文字左边缘的x值，y为文字的baseline的y值。
        // paint.ascent()获得baseline到文字上边缘的值，
        // paint.descent()获得baseline到文字下边缘
        if (mStyle == STYLE_STROKE) {
            paint.color = textColor
            paint.style = Paint.Style.STROKE
        } else {
            paint.color = bgColor
            paint.style = Paint.Style.FILL
        }
        canvas.drawRoundRect(rect, radius, radius, paint)

        // 绘制字
        newPaint.isAntiAlias = true
        newPaint.color = textColor
        val fm = newPaint.fontMetrics
        canvas.drawText(
            text,
            start,
            end,
            x + marginLeft,
            bottom - offsetHeight - marginBottom - fm.bottom,
            newPaint
        )
    }

    private fun getHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.bottom - fm.top
    }

    private fun getCustomTextPaint(srcPaint: Paint): TextPaint {
        val textPaint = TextPaint(srcPaint)
        textPaint.textSize = fontSize
        return textPaint
    }

    companion object {
        const val STYLE_FILL = 0 //填充
        const val STYLE_STROKE = 1 //扫边。扫边颜色默认和字体颜色一致
    }
}