package com.example.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.pushstream.PushStreamActivity
import com.example.compose.ui.multivoicemanager.LiveMulVoiceManagerActivity
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.ui.videodetail.BiliVideoDetailActivity

class MainActivity : BaseActivity() {
    private val items = listOf<Class<*>>(
        LiveMulVoiceManagerActivity::class.java,
        BiliVideoDetailActivity::class.java,
        XmlActivity::class.java,
        LiveFeedActivity::class.java,
        PushStreamActivity::class.java
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                MainFunctionList(this@MainActivity)
            }
        }
    }

    // @Preview(name = "MainFunctionList")
    @Composable
    private fun MainFunctionList(context: Context) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            itemsIndexed(items = items) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { // startActivity(Intent(LocalContext.current, item))
                            startActivity(Intent(context, item))
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 50.dp, end = 50.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = "$index ${item.simpleName}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Preview(name = "testLazyColumn")
@Composable
private fun TestLazyColumn() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp), text = "HEADER"
            )
        }

        // items(count = 20) {
        //     Text(
        //         modifier = Modifier
        //             .fillMaxWidth()
        //             .height(60.dp),
        //         text = "POS = $it"
        //     )
        // }

        val list = mutableListOf<String>().apply {
            repeat(20) {
                add(it.toString())
            }
        }
        itemsIndexed(items = list) { index, item ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp), text = "POS = $item"
            )
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp), text = "FOOTER"
            )
        }
    }
}

@Composable
@Preview(name = "TestRipple")
private fun TestRipple() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val modifier = Modifier
        Log.d("Test.Ripple", "modifier: $modifier, hashCode: ${modifier.hashCode()}")

        val modifier2 = modifier
            .width(280.dp)
            .noRippleClickable2 {
                Log.d("Test.Ripple", "TestRipple click text")
            }
        Log.d("Test.Ripple", "modifier2: $modifier, hashCode: ${modifier.hashCode()}")
        Text(
            modifier = modifier2,
            text = "为保护您的权益，请点击下方按钮进行安全验证，验证成功后即可正常观看",
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

fun Modifier.noRippleClickable2(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed(inspectorInfo = debugInspectorInfo {
    name = "clickable"
    properties["enabled"] = enabled
    properties["onClickLabel"] = onClickLabel
    properties["role"] = role
    properties["onClick"] = onClick
}) {
    this.clickable(enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = onClick,
        role = role,
        indication = null,
        interactionSource = remember { MutableInteractionSource() })
}