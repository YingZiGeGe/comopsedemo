package com.example.compose.pack

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.compose.R
import kotlin.random.Random

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-11-09
 * @desc   :
 *
 */
private const val TAG = "Cheese.Package"

@Preview(name = "CheesePackageFloor")
@Composable
private fun CheesePackageFloor() {
    ConstraintLayout(
        modifier = Modifier
            .wrapContentSize()
            .background(color = Color(0xFFE3E5E7))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(top = 3.dp, bottom = 3.dp)
                .fillMaxWidth()
                .height(162.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            val (title, subtitle, more, moreIcon, items) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    }
                    .wrapContentSize(),
                text = "套餐优惠",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF18191C),
            )

            Text(modifier = Modifier
                .constrainAs(subtitle) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    start.linkTo(title.end, margin = 8.dp)
                }
                .wrapContentSize()
                .background(color = Color(0xFFFFF1ED), shape = RoundedCornerShape(2.dp))
                .padding(start = 3.dp, end = 3.dp),
                text = "联报限时折上折",
                letterSpacing = 0.sp,
                fontSize = 10.sp,
                color = Color(0xFFFF6633))

            Text(modifier = Modifier
                .constrainAs(more) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(moreIcon.start, margin = 2.dp)
                }
                .wrapContentSize(), text = "全部15门", fontSize = 10.sp, color = Color.Gray)

            Image(modifier = Modifier
                .constrainAs(moreIcon) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(parent.end, margin = 10.dp)
                }
                .size(16.dp),
                painter = painterResource(id = R.drawable.ic_gift),
                contentDescription = null)

            val mockList = ArrayList<Int>().apply {
                repeat(5) {
                    this.add(it)
                }
            }
            LazyRow(modifier = Modifier.constrainAs(items) {
                linkTo(
                    top = title.bottom,
                    bottom = parent.bottom,
                    start = parent.start,
                    end = parent.end,
                    topMargin = 10.dp,
                    horizontalBias = 0F,
                    verticalBias = 0F,
                )
            }) {
                itemsIndexed(items = mockList) { index, item ->
                    Log.d(TAG, "Cheese.Package CheesePackageFloor index: $index, item: $item")
                    Row {
                        // 间距
                        Spacer(modifier = Modifier.width(10.dp))
                        CheesePackageItem(index)
                        if (index == mockList.size - 1) {
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }
        }
    }
}

// @Preview(name = "CheesePackageItem")
@Composable
private fun CheesePackageItem(index: Int) {
    ConstraintLayout(
        modifier = Modifier
            .width(128.dp)
            .wrapContentHeight()
            .background(color = Color.White)
    ) {
        val (image, label, imageColor, imageText, title) = createRefs()

        Image(modifier = Modifier
            .constrainAs(image) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .width(128.dp)
            .height(72.dp),
            painter = painterResource(id = R.drawable.ic_comic_header),
            contentScale = ContentScale.Crop,
            contentDescription = null)

        Text(modifier = Modifier
            .constrainAs(label) {
                top.linkTo(image.top, margin = 4.dp)
                end.linkTo(image.end, margin = 4.dp)
            }
            .wrapContentSize()
            .background(color = Color(0xFFFF6699), shape = RoundedCornerShape(2.dp))
            .padding(start = 3.dp, end = 3.dp),
            text = "套餐",
            fontSize = 10.sp,
            color = Color.White)

        Canvas(modifier = Modifier
            .constrainAs(imageColor) {
                bottom.linkTo(image.bottom)
                start.linkTo(image.start)
                end.linkTo(image.end)
            }
            .fillMaxWidth()
            .height(30.dp), onDraw = {
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color(0x80000000))
                )
            )
        })

        Text(modifier = Modifier
            .constrainAs(imageText) {
                linkTo(
                    top = image.top,
                    bottom = image.bottom,
                    start = image.start,
                    end = image.end,
                    verticalBias = 1F,
                    horizontalBias = 1F,
                    bottomMargin = 4.dp,
                    endMargin = 4.dp
                )
            }
            .wrapContentSize(),
            text = "共2门课",
            color = Color.White,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)

        Text(modifier = Modifier
            .constrainAs(title) {
                top.linkTo(image.bottom, margin = 6.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(image.start)
                end.linkTo(image.end)
            }
            .wrapContentSize(),
            text = if (index == 5) "2022高考数学新一轮总复习极客时间" else "2022高考数学",
            fontSize = 12.sp,
            color = Color(0xFF18191C),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis)
    }
}

// 套餐浮层
@Preview(name = "CheesePackageItemLayer")
@Composable
private fun CheesePackageLayer() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val (titleBox, line, items) = createRefs()

        ConstraintLayout(modifier = Modifier
            .constrainAs(titleBox) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth()
            .height(44.dp)) {
            val (title, close) = createRefs()

            Text(modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 12.dp)
                }
                .wrapContentSize(), text = "套餐优惠")

            Image(modifier = Modifier
                .constrainAs(close) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(parent.end, margin = 10.dp)
                }
                .size(16.dp),
                painter = painterResource(id = R.drawable.ic_gift),
                contentDescription = null)
        }

        Spacer(modifier = Modifier
            .constrainAs(line) {
                top.linkTo(titleBox.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth()
            .height(1.dp.div(5))
            .background(color = Color.Gray))

        val mockList = ArrayList<Int>().apply {
            repeat(10) {
                add(it)
            }
        }
        LazyColumn(modifier = Modifier
            // .padding(bottom = 44.dp)
            .fillMaxWidth()
            .constrainAs(items) {
                top.linkTo(line.bottom)
                // bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            itemsIndexed(items = mockList) { index, item ->
                CheesePackageLayerItem()
            }
        }
    }
}

@Preview(name = "CheesePackageLayerItem")
@Composable
private fun CheesePackageLayerItem() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .background(color = Color.White)
    ) {
        val (image, label, title, desc, priceRow, line, test) = createRefs()

        Image(modifier = Modifier
            .constrainAs(image) {
                top.linkTo(parent.top, margin = 10.dp)
                start.linkTo(parent.start, margin = 10.dp)
            }
            .width(128.dp)
            .height(72.dp)
            .clip(shape = RoundedCornerShape(4.dp)),
            painter = painterResource(id = R.drawable.ic_comic_header),
            contentScale = ContentScale.Crop,
            contentDescription = null)

        Text(modifier = Modifier
            .constrainAs(label) {
                top.linkTo(image.top, margin = 3.dp)
                end.linkTo(image.end, margin = 3.dp)
            }
            .wrapContentSize()
            .background(color = Color.Magenta, shape = RoundedCornerShape(4.dp))
            .padding(start = 2.dp, end = 2.dp),
            text = "套餐",
            color = Color.White,
            fontSize = 10.sp)

        // Image(
        //     modifier = Modifier.constrainAs(test) {
        //         top.linkTo(parent.top)
        //         bottom.linkTo(parent.bottom)
        //         end.linkTo(parent.end)
        //     }.size(20.dp),
        //     painter = painterResource(id = R.drawable.ic_gift),
        //     contentDescription = null)

        // title
        Text(modifier = Modifier
            .padding(start = 148.dp, end = 10.dp)
            .constrainAs(title) {
                linkTo(
                    top = image.top,
                    bottom = desc.top,
                    start = parent.start,
                    end = parent.end,
                    startMargin = 0.dp,
                    endMargin = 0.dp,
                    horizontalBias = 0F,
                    verticalBias = 0F,
                )
            }
            .fillMaxWidth()
            .wrapContentHeight(),
            text = "政治英语数学刷押联报政治英语数学刷押联报政治英语数学刷押联报政治英语数学刷押联报政治英语数学刷押联报政治英语数学刷押联报",
            color = Color.Black,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2)

        Row(modifier = Modifier.constrainAs(priceRow) {
            bottom.linkTo(image.bottom)
            start.linkTo(title.start)
        }) {
            // discount price
            val discountPriceTxt = "券后949元"
            if (discountPriceTxt.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 148.dp)
                        .wrapContentSize(),
                    text = discountPriceTxt,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                        .wrapContentHeight()
                )
            }

            // original price
            val textDecoration =
                if (discountPriceTxt.isNotEmpty()) TextDecoration.LineThrough else TextDecoration.None
            Text(
                modifier = Modifier.wrapContentSize(),
                text = "原价1664元",
                fontSize = 12.sp,
                color = Color.Gray,
                textDecoration = textDecoration
            )
        }

        // desc
        Text(modifier = Modifier
            .padding(start = 148.dp, end = 10.dp)
            .constrainAs(desc) {
                bottom.linkTo(priceRow.top)
                start.linkTo(title.start)
            }
            .wrapContentSize(), text = "共3门课程", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier
            .constrainAs(line) {
                top.linkTo(image.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth()
            .height(1.dp.div(2))
            .background(color = Color.Gray))
    }
}