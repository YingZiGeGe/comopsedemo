package com.example.compose

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var ivLogo: ImageView
    private lateinit var tvBottomEndText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_feed_activity)

        initView()
    }

    private fun initView() {
        clRoot = findViewById(R.id.cl_root)
        tvHelloWorld = findViewById(R.id.tv_hello)
        ivLogo = findViewById(R.id.iv_logo)
        tvBottomEndText = findViewById(R.id.tv_bottom_end_text)

        ivLogo.setOnClickListener {
            ivLogo.imageTintList = null
        }

        val flowTest = MutableStateFlow(0)

        this.lifecycleScope.launch {
            flowTest.collectLatest {
                Log.d(TAG, "flowTest collect value: $it")

                coroutineScope {
                    awaitCancel {
                        testSus(it)
                    }
                }
            }
        }

        var num = 0
        tvHelloWorld.setOnClickListener {
            this.lifecycleScope.launch {
                flowTest.emit(++num)
            }
        }
    }

    private fun testSus(value: Int) {
        Log.d(TAG, "testSus withContext execute, value: $value")
    }

    companion object {
        private const val TAG = "Cheese.Bug"
    }
}

inline fun CoroutineScope.awaitCancel(crossinline block: () -> Unit) {
    launch {
        try {
            awaitCancellation()
        } finally {
            block()
        }
    }
}