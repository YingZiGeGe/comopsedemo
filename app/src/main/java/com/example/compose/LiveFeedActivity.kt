package com.example.compose

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.compose.utils.PixelUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-10-23
 * @desc   : 直播 feed 流 activity, 测试数据变化时(网上添加数据时), pos 是否会自动重新定位
 *
 */
class LiveFeedActivity : BaseActivity() {
    private lateinit var clRoot: ConstraintLayout
    private lateinit var tvHelloWorld: TextView
    private lateinit var tvShare: TextView
    private lateinit var sharePopupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_feed_activity)

        initView()
    }

    private fun initView() {
        clRoot = findViewById(R.id.cl_root)
        tvHelloWorld = findViewById(R.id.tv_hello)
        tvShare = findViewById(R.id.tv_share)
        val rect = Rect()

        tvShare.post {
            val isVisible = tvShare.getGlobalVisibleRect(rect)
            if (isVisible) {
                Log.d(TAG, "rect: $rect")
            }
        }

        initPopupWindow()
        tvHelloWorld.setOnClickListener {
            if (sharePopupWindow.isShowing) {
                sharePopupWindow.dismiss()
                return@setOnClickListener
            }
            sharePopupWindow.showAsDropDown(
                tvShare,
                -PixelUtil.dp2px(this@LiveFeedActivity, (149F - 36F)) + (rect.width() / 2),
                -(rect.height() + PixelUtil.dp2px(this@LiveFeedActivity, 50F))
            )
        }

        tvShare.setOnClickListener {

        }
    }

    private fun initPopupWindow() {
        val contentView = LayoutInflater.from(this@LiveFeedActivity)
            .inflate(R.layout.ugc_share_bubble_pop_layout, null, false)
        sharePopupWindow = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            this.isOutsideTouchable = true
        }
    }

    companion object {
        private const val TAG = "UGC.Share"
    }
}

interface SelectCancellingUnselectedScope<in R> {
    fun selectAsync(block: suspend () -> R)

    fun selectFirstOf(flow: Flow<R>)
}

suspend fun <R> selectCancellingUnselected(builder: SelectCancellingUnselectedScope<R>.() -> Unit): R {
    val flows = arrayListOf<Flow<R>>()
    val scope = object : SelectCancellingUnselectedScope<R> {
        override fun selectAsync(block: suspend () -> R) {
            flows += flow { emit(block()) }
        }

        override fun selectFirstOf(flow: Flow<R>) {
            flows += flow
        }
    }
    scope.builder()
    return flows.merge().first()
}