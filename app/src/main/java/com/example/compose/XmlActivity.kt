package com.example.compose

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-01-12
 * @desc   : 经过测试, 发现在 view 不在前台时, 数据可以赋值进来, 但 view 不会重新测量宽高, 只有回到前台后才会重新测量
 *
 */
class XmlActivity : ComponentActivity() {
    private var tvText: TextView? = null
    private var bottomContainer: FrameLayout? = null

    private val uiState = TestBottomData(onClick = {
        reqFavorite()
    })
    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml)

        initView()
    }

    private fun initView() {
        tvText = findViewById(R.id.tv_text)
        bottomContainer = findViewById(R.id.bottom_container)
        val composeView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TestBottomView(data = uiState)
            }
        }
        bottomContainer?.addView(composeView)
    }

    private fun reqFavorite() {
        lifecycleScope.launch {
            delay(100)
            num++
            if (num % 2 == 0) {
                uiState.apply {
                    this.selected = false
                    this.icon = R.drawable.ic_comic_header
                    this.text = "收藏"
                }
            } else {
                uiState.apply {
                    this.selected = true
                    this.icon = R.drawable.ic_gift
                    this.text = "已收藏"
                }
            }
        }
    }
}

@Composable
private fun TestBottomView(data: TestBottomData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) { // 收藏
            Column(modifier = Modifier
                .size(60.dp)
                .clickable {
                    data.onClick.invoke()
                }) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = data.icon),
                    contentDescription = ""
                )

                Text(
                    modifier = Modifier
                        .width(60.dp)
                        .wrapContentHeight(),
                    text = data.text,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Stable
data class TestBottomData(
    val onClick: () -> Unit = {}
) {
    var selected: Boolean by mutableStateOf(false)
    @get:DrawableRes
    var icon: Int by mutableStateOf(R.drawable.ic_comic_header)
    var text: String by mutableStateOf("收藏")
}