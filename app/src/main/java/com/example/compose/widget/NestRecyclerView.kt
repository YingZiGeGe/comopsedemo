package com.example.compose.widget

import android.content.Context
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-10-24
 * @desc   :
 *
 */
class NestRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mUserInputEnabled = true

    // Fling friction, copy from OverScroller.java
    private var mFlingFriction = ViewConfiguration.getScrollFriction()
    private val mDecelerationRate = (Math.log(0.78) / Math.log(0.9)).toFloat()

    // A context-specific coefficient adjusted to physical values.
    private var mPhysicalCoeff = 0f

    /**
     * Enable or disable user initiated scrolling. This includes touch input (scroll and fling
     * gestures) and accessibility input. Disabling keyboard input is not yet supported. When user
     * initiated scrolling is disabled, programmatic scrolls through [setCurrentItem][.setCurrentItem] still work. By default, user initiated scrolling is enabled.
     *
     * @param enabled `true` to allow user initiated scrolling, `false` to block user
     * initiated scrolling
     * @see .isUserInputEnabled
     */
    fun setUserInputEnabled(enabled: Boolean) {
        mUserInputEnabled = enabled
    }

    /**
     * Returns if user initiated scrolling between pages is enabled. Enabled by default.
     *
     * @return `true` if users can scroll the ViewPager2, `false` otherwise
     * @see .setUserInputEnabled
     */
    private fun isUserInputEnabled(): Boolean {
        return mUserInputEnabled
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return isUserInputEnabled() && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isUserInputEnabled() && super.onInterceptTouchEvent(ev)
    }

    init {
        val ppi = context.resources.displayMetrics.density * 160.0f
        mPhysicalCoeff = (SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi
                * 0.84f) // look and feel tuning
    }

    private fun getSplineDeceleration(velocity: Int): Double {
        return  Math.log(INFLEXION * abs(velocity) / (mFlingFriction * mPhysicalCoeff).toDouble())
    }

    fun getSplineFlingDistance(velocity: Int): Double {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne: Double = mDecelerationRate - 1.0
        return mFlingFriction * mPhysicalCoeff * Math.exp(mDecelerationRate / decelMinusOne * l)
    }

    companion object {
        const val INFLEXION = 0.35f
    }
}
