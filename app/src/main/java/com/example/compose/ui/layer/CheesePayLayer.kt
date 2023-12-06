package com.example.compose.ui.layer

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.compose.R

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-08-02
 * @desc   :
 *
 */
// @Preview(name = "CheesePayLayerCompose")
@Composable
fun CheesePayLayerView(playerMask: PlayerMask) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        val (back, title, content, button) = createRefs() // 目前发现约束布局中使用链会导致 margin 失效, 参考 [https://github.com/androidx/constraintlayout/issues/572],
        // 所以这里又包了一层...

        // 创建约束链
        // createVerticalChain(
        //     title, content, button, chainStyle = ChainStyle.Packed
        // )

        // 返回按钮
        if (true) {
            Image(
                modifier = Modifier
                    .constrainAs(back) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    }
                    .size(24.dp)
                    .clickable { // todo Cheese.Ship activity back
                    },
                painter = painterResource(id = R.drawable.ic_comic_header),
                contentDescription = null,
            )
        }

        val contentLayout = createRef()
        ConstraintLayout(modifier = Modifier
            .constrainAs(contentLayout) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .wrapContentSize()) { // title
            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(content.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .wrapContentSize(),
                text = playerMask.title,
                color = Color.White,
                fontSize = 16.sp,
            )

            val spanString = SpannableStringBuilder()
            spanString.append(playerMask.prefix).append(playerMask.priceText)
                .append(playerMask.suffix) // val colorSpan = ForegroundColorSpan(android.graphics.Color.parseColor("0xFFFF6699"))
            val colorSpan = ForegroundColorSpan(android.graphics.Color.BLUE)
            spanString.setSpan(
                colorSpan,
                playerMask.prefix.length,
                playerMask.prefix.length + playerMask.priceText.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            Text(
                modifier = Modifier
                    .constrainAs(content) {
                        top.linkTo(title.bottom, margin = 5.dp)
                        bottom.linkTo(button.top)
                        start.linkTo(title.start)
                        end.linkTo(title.end)
                    }
                    .wrapContentSize(),
                text = buildAnnotatedString {

                    append(playerMask.prefix)

                    pushStyle(SpanStyle(color = Color(0xFFFF6699)))
                    append(playerMask.priceText)
                    pop()

                    append(playerMask.suffix)
                },
                color = Color.White,
                fontSize = 14.sp,
            )

            Text(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(content.bottom, margin = 15.dp)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(title.start)
                        end.linkTo(title.end)
                    }
                    .wrapContentSize()
                    .background(
                        color = Color(0xFFFF6699), shape = RoundedCornerShape(20.dp)
                    )
                    .padding(
                        start = 25.dp, top = 5.dp, end = 25.dp, bottom = 5.dp
                    )
                    .clickable {
                        playerMask.onClickPay.invoke()
                    },
                text = playerMask.buttonText,
                color = Color.White,
                fontSize = 14.sp,
            )
        }
    }
}

data class PlayerMask(
    val title: String,
    val prefix: String,
    val suffix: String,
    val priceText: String,
    val buttonText: String,
    val onClickPay: () -> Unit
)

@Preview(name = "BlurImage")
@Composable
private fun BlurImage() {
    Box(modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_comic_header),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .blur(16.dp)
        )

        // 前景色
        Spacer(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .background(color = Color(0x99000000))
        )
    }
}