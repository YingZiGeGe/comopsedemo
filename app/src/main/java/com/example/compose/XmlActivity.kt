package com.example.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-01-12
 * @desc   : 经过测试, 发现在 view 不在前台时, 数据可以赋值进来, 但 view 不会重新测量宽高, 只有回到前台后才会重新测量
 *
 */
class XmlActivity : ComponentActivity() {
    private var rootView: LinearLayout? = null
    private var tvText: TextView? = null
    private val titles = mutableListOf("这", "城市", "已摊开", "她孤独的", "地图我怎么能", "找到你等我的地", "方, 我像买个恋爱的孩子一样")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml)

        initView()
    }

    private fun initView() {
        rootView = findViewById(R.id.root_view)
        tvText = findViewById(R.id.tv_text)

        GlobalScope.launch(Dispatchers.Main) {
            repeat(Int.MAX_VALUE) {
                delay(1000)
                tvText?.text = titles[Random.nextInt(0, titles.size - 1)]
                Log.d("XmlActivity", "Simon.Debug tvText = ${tvText?.text}, width = ${tvText?.measuredWidth}")
            }
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, XmlActivity::class.java))
        }
    }
}