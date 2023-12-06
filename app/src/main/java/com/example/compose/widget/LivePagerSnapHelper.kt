package com.example.compose.widget

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.OrientationHelper
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
class LivePagerSnapHelper(val recyclerView: RecyclerView) : LiveBaseSnapHelper() {

    // Orientation helpers are lazily created per LayoutManager.
    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null

    companion object {
        private const val FLING_LOG_TAG = "fling_opt"
    }

    override fun createSnapScroller(switchLiveAnimRatio: Float, layoutManager: RecyclerView.LayoutManager?): LinearSmoothScroller? {
        return if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            LinearSmoothScroller(recyclerView.context)
        } else object : LiveSmoothScroller(switchLiveAnimRatio,recyclerView.context) {

            override fun onTargetFound(targetView: View, state: RecyclerView.State, action: RecyclerView.SmoothScroller.Action) {
                val snapDistances = calculateDistanceToFinalSnap(layoutManager, targetView)
                    ?: return
                val dx = snapDistances[0]
                val dy = snapDistances[1]
                val time = calculateTimeForDeceleration(abs(dx).coerceAtLeast(abs(dy)))
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator)
                }
            }

            override fun calculateTimeForScrolling(dx: Int): Int {
                var newDx = dx
                val orientationHelper = getOrientationHelper(layoutManager)
                orientationHelper?.let {
                    // 回弹动画离顶部或底部距离
                    val switchLiveDistance = orientationHelper.totalSpace * switchLiveAnimRatio
                    newDx = (dx * if (abs(dx) > switchLiveDistance) 1 else 4)
                }
                return super.calculateTimeForScrolling(newDx)
            }
        }
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager,
                                              targetView: View
    ): IntArray? {
        val out = IntArray(2)
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager, xOrientation: Int, yOrientation: Int): View? {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager), yOrientation)
        } else if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager), xOrientation)
        }
        return null
    }

    /**
     * 这个方法逻辑需要重写，满足产品需求，在触发惯性滑动的时候，如果下个直播间距离比较远，不应该滑到下个直播间
     * 所以在此方法里增加此逻辑
     */
    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int,
                                        velocityY: Int): Int {
        val itemCount = layoutManager.itemCount
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION
        }
        val orientationHelper = getOrientationHelper(layoutManager) ?: return RecyclerView.NO_POSITION
        val velFlingDistance: Int = (recyclerView as? NestRecyclerView)?.getSplineFlingDistance(velocityY)?.toInt() ?: 0
        var targetView = flingFindCenterView(layoutManager, orientationHelper, velocityY, velFlingDistance)
        targetView?.let {
            return layoutManager.getPosition(targetView)
        }
        return RecyclerView.NO_POSITION
    }


    private fun distanceToCenter(layoutManager: RecyclerView.LayoutManager,
                                 targetView: View, helper: OrientationHelper
    ): Int {
        val childCenter = (helper.getDecoratedStart(targetView)
                + helper.getDecoratedMeasurement(targetView) / 2)
        val containerCenter = helper.startAfterPadding + helper.totalSpace / 2
        return childCenter - containerCenter
    }

    private fun flingFindCenterView(layoutManager: RecyclerView.LayoutManager,
                                    helper: OrientationHelper, velocityY: Int, velFlingDistance: Int): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }
        var closestChild: View? = null
        val center = helper.startAfterPadding + (helper.totalSpace *
                if (if (mSumDy == 0) velocityY < 0 else mSumDy < 0) 0.3F else (1 - 0.3F)).toInt()
        var flingDistance = velFlingDistance
        if (flingDistance > helper.totalSpace / 2) {//根据速率换算出的滑动距离，不能超过直播间高度的1/2
            flingDistance = helper.totalSpace / 2
        }
        var absClosest = Int.MAX_VALUE
        for (i in 0 until childCount) {// scrollOrientation >=0 上滑，scrollOrientation < 下滑
            val child = layoutManager.getChildAt(i)
            var childCenter = (helper.getDecoratedStart(child) + helper.getDecoratedMeasurement(child) / 2)
            if (velocityY >= 0) {
                childCenter -= flingDistance
            } else {
                childCenter += flingDistance
            }
            val absDistance = abs(childCenter - center)
            val flingRatio = if (mSumDy > 0) {
                (childCenter.toDouble() / helper.totalSpace - 0.5) * 100
            } else {
                (0.5 - childCenter.toDouble() / helper.totalSpace) * 100
            }
            /* if child center is closer than previous closest, set it as closest  */
            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }


    /**
     * Return the child view that is currently closest to the center of this parent.
     *
     * @param layoutManager The [RecyclerView.LayoutManager] associated with the attached
     * [RecyclerView].
     * @param helper The relevant [OrientationHelper] for the attached [RecyclerView].
     *
     * @return the child view that is currently closest to the center of this parent.
     */
    private fun findCenterView(layoutManager: RecyclerView.LayoutManager,
                               helper: OrientationHelper, scrollOrientation: Int): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }
        var closestChild: View? = null
        val center = helper.startAfterPadding + (helper.totalSpace *
                if (scrollOrientation < 0) 0.4F else (1 - 0.4F)).toInt()
        var absClosest = Int.MAX_VALUE
        for (i in 0 until childCount) {// scrollOrientation >0 下滑，scrollOrientation <=0 上滑
            val child = layoutManager.getChildAt(i)
            val childCenter = (helper.getDecoratedStart(child) + helper.getDecoratedMeasurement(child) / 2)
            val absDistance = abs(childCenter - center)
            val slowFlingRatio = if (scrollOrientation > 0) {
                (childCenter.toDouble() / helper.totalSpace - 0.5) * 100
            } else {
                (0.5 - childCenter.toDouble() / helper.totalSpace) * 100
            }
            if (slowFlingRatio != 0.00) {//没滑动的日志不输出

            }
            /* if child center is closer than previous closest, set it as closest  */
            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }

    private fun getOrientationHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper? {
        return when {
            layoutManager.canScrollVertically() -> {
                getVerticalHelper(layoutManager)
            }
            layoutManager.canScrollHorizontally() -> {
                getHorizontalHelper(layoutManager)
            }
            else -> {
                null
            }
        }
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mVerticalHelper == null || mVerticalHelper?.layoutManager !== layoutManager) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper!!
    }

    private fun getHorizontalHelper(
        layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mHorizontalHelper == null || mHorizontalHelper?.layoutManager !== layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }

}

open class LiveSmoothScroller(val switchLiveAnimRatio: Float, val context: Context) : LinearSmoothScroller(context) {

}