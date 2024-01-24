/*
 * Copyright (c) 2015-2021 BiliBili Inc.
 */

package com.example.compose.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.compose.R
import kotlin.math.abs

/**
 * Created by 陈小狮(chentingjia@bilibili.com) on 2021/11/10
 *
 * Description：-
 */
const val POSITION_UP = 0
const val POSITION_DOWN = 1

const val RENDER_STROKE = 0
const val RENDER_FILL = 1

open class BubbleBackgroundConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var mCornerRadius = 8f //圆角
        set(value) {
            field = value
            invalidate()
        }
    var mRenderColor = Color.GRAY //渲染颜色
        set(value) {
            field = value
            invalidate()
        }
    var mTriangleHorizonCenterPosition = 8f //三角中心点水平位置：正数以左侧为原点，负数以右侧为原点
        set(value) {
            field = value
            invalidate()
        }
    var mTriangleVerticalLocation = POSITION_UP //三角锚点垂直方向的位置
        set(value) {
            field = value
            invalidate()
        }
    var mRenderMode = RENDER_STROKE //渲染模式：stroke-描边，fill-填充
        set(value) {
            field = value
            invalidate()
        }
    var mRenderStrokeWidth = 2f //描边宽度
        set(value) {
            field = value
            invalidate()
        }
    var mTriangleWidth = 24f //三角锚点的宽
        set(value) {
            field = value
            invalidate()
        }
    var mTriangleHeight = 18f //三角锚点的高
        set(value) {
            field = value
            invalidate()
        }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleBackgroundConstraintLayout)
        mCornerRadius =
            ta.getDimensionPixelSize(R.styleable.BubbleBackgroundConstraintLayout_corner_radius, 4.toPx()).toFloat()
        mTriangleHorizonCenterPosition =
            ta.getDimensionPixelSize(
                R.styleable.BubbleBackgroundConstraintLayout_triangle_horizon_center_position,
                4.toPx()
            ).toFloat()
        mTriangleVerticalLocation =
            ta.getInt(R.styleable.BubbleBackgroundConstraintLayout_triangle_vertical_location, POSITION_UP)
        mRenderStrokeWidth =
            ta.getDimensionPixelSize(R.styleable.BubbleBackgroundConstraintLayout_stroke_width, 1.toPx()).toFloat()
        mTriangleWidth =
            ta.getDimensionPixelSize(R.styleable.BubbleBackgroundConstraintLayout_triangle_width, 12.toPx()).toFloat()
        mTriangleHeight =
            ta.getDimensionPixelSize(R.styleable.BubbleBackgroundConstraintLayout_triangle_height, 9.toPx()).toFloat()
        mRenderColor = ta.getColor(R.styleable.BubbleBackgroundConstraintLayout_render_color, Color.GRAY)
        mRenderMode = ta.getInt(R.styleable.BubbleBackgroundConstraintLayout_render_mode, RENDER_STROKE)
        ta.recycle()
    }

    var toErase = false
        set(value) {
            field = value
            invalidate()
        }
    private val mErase by lazy {
        Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }
    protected val mTrianglePath by lazy { Path() }
    protected val mPath by lazy { Path() }
    private val mRectF by lazy { RectF() }
    private val mPaint by lazy { Paint().apply { isAntiAlias = true } }

    override fun dispatchDraw(canvas: Canvas) {
        drawBackground(canvas)
        super.dispatchDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        if (toErase || height == 0 || width == 0) {
            canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mErase)
            return
        }

        mPath.rewind()
        computeRectPath()
        computeTrianglePath()
        mPath.op(mTrianglePath, Path.Op.UNION)

        resetPaint()
        canvas?.drawPath(mPath, mPaint)
    }

    private fun resetPaint() {
        mPaint.apply {
            reset()
            isAntiAlias = true
            style = if (mRenderMode == RENDER_FILL) Paint.Style.FILL else Paint.Style.STROKE
            strokeWidth = mRenderStrokeWidth
            color = mRenderColor
        }
    }

    protected open fun computeRectPath() {
        mRectF.setEmpty()
        mRectF.set(
            0f,
            if (mTriangleVerticalLocation == POSITION_UP) mTriangleHeight else 0f,
            width.toFloat(),
            if (mTriangleVerticalLocation == POSITION_UP) height.toFloat() else height - mTriangleHeight
        )
        mPath.addRoundRect(
            mRectF,
            floatArrayOf(
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius,
                mCornerRadius
            ),
            Path.Direction.CW
        )
    }

    protected open fun computeTrianglePath() {
        mTrianglePath.rewind()
        val r = abs(mTriangleWidth) / 2
        val startX = when {
            mTriangleHorizonCenterPosition >= r -> {
                mTriangleHorizonCenterPosition - r
            }
            mTriangleHorizonCenterPosition <= -r -> {
                width + mTriangleHorizonCenterPosition - r
            }
            else -> {
                0f
            }
        }
        val startY = if (mTriangleVerticalLocation == POSITION_UP) mTriangleHeight else height - mTriangleHeight
        val endY = if (mTriangleVerticalLocation == POSITION_UP) 0f else height.toFloat()
        mTrianglePath.moveTo(startX, startY)
        mTrianglePath.lineTo(startX + r, endY)
        mTrianglePath.lineTo(startX + 2 * r, startY)
    }
}

fun Int.toPx(): Int {
    return (this * (Resources.getSystem().displayMetrics?.density ?: 2f) + 0.5f).toInt()
}