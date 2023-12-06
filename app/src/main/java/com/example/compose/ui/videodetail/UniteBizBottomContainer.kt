package com.example.compose.ui.videodetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.compose.R
import kotlinx.coroutines.launch

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-08-06
 * @desc   :
 *
 */
private val ITEM_TEXT_SIZE = 10.sp
private val ITEM_TEXT_COLOR = Color(0xFF61666D)
private val ITEM_MARGIN_PADDING = 10.dp
private val ITEM_MARGIN_START = 10.dp

// @Preview(name = "UniteBizBottomContainer")
@Composable
fun UniteBizBottomContainer() {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
    ) {
        // 收藏, 课堂群, 咨询, 课件, 分享, 充值, 支付
        val (favorite, favoriteIcon, favoriteText, group, groupIcon, groupText, consult, consultIcon, consultText) = createRefs()
        val (courseware, coursewareIcon, coursewareText, share, shareIcon, shareText, chargeLayout, chargeText, balanceText, payLayout) = createRefs()

        // 设置 chain 会导致 marin 失效...
        // 设置总 chain
        createHorizontalChain(
            favorite,
            group,
            consult,
            courseware,
            share,
            payLayout,
            chainStyle = ChainStyle.Packed
        )
        // 设置 充值 layout chain
        createVerticalChain(chargeText, balanceText, chainStyle = ChainStyle.Packed)
        // 设置充值, 支付 chain
        createHorizontalChain(chargeLayout, payLayout, chainStyle = ChainStyle.SpreadInside)


        // 收藏
        val hasFavorite = true
        if (hasFavorite) {
            Column(
                modifier = Modifier
                    .padding(start = ITEM_MARGIN_START)
                    .constrainAs(favorite) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(group.start)
                    }
                    .size(40.dp)
                    .background(color = Color.Red)
                    .clickable {

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val drawable =
                    if (true) R.drawable.ic_comic_header else R.drawable.ic_multi_voice_setting
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = drawable),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier, text = "收藏", fontSize = 10.sp, color = ITEM_TEXT_COLOR
                )
            }
        }

        // 课堂群
        val hasGroup = false
        if (hasGroup) {
            Column(
                modifier = Modifier
                    .padding(start = ITEM_MARGIN_PADDING)
                    .constrainAs(group) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(favorite.end)
                    }
                    .size(40.dp)
                    .background(color = Color.Red)
                    .clickable {

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val drawable =
                    if (true) R.drawable.ic_comic_header else R.drawable.ic_multi_voice_setting
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = drawable),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier, text = "课堂群", fontSize = 10.sp, color = ITEM_TEXT_COLOR
                )
            }
        }

        // 咨询
        val hasConsult = true
        if (hasConsult) {
            Column(
                modifier = Modifier
                    .padding(start = ITEM_MARGIN_PADDING)
                    .constrainAs(consult) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(group.end)
                    }
                    .size(40.dp)
                    .background(color = Color.Red)
                    .clickable {

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val drawable =
                    if (true) R.drawable.ic_comic_header else R.drawable.ic_multi_voice_setting
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = drawable),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier, text = "咨询", fontSize = 10.sp, color = ITEM_TEXT_COLOR
                )
            }
        }

        // 课件
        val hasCourseware = false
        if (hasCourseware) {
            Column(
                modifier = Modifier
                    .padding(start = ITEM_MARGIN_PADDING)
                    .constrainAs(courseware) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(consult.end)
                    }
                    .size(40.dp)
                    .background(color = Color.Red)
                    .clickable {

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val drawable =
                    if (true) R.drawable.ic_comic_header else R.drawable.ic_multi_voice_setting
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = drawable),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier, text = "课件", fontSize = 10.sp, color = ITEM_TEXT_COLOR
                )
            }
        }

        // 分享
        val hasShare = true
        if (hasShare) {
            Column(
                modifier = Modifier
                    .padding(start = ITEM_MARGIN_PADDING)
                    .constrainAs(share) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(courseware.end)
                    }
                    .size(40.dp)
                    .background(color = Color.Red)
                    .clickable {

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val drawable =
                    if (true) R.drawable.ic_comic_header else R.drawable.ic_multi_voice_setting
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = drawable),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier, text = "分享", fontSize = 10.sp, color = ITEM_TEXT_COLOR
                )
            }
        }

        // 充值
        val hasCharge = false
        if (hasCharge) {
            ConstraintLayout(modifier = Modifier
                .padding(start = 10.dp)
                .constrainAs(chargeLayout) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }) {
                Text(modifier = Modifier.constrainAs(chargeText) {
                    linkTo(
                        top = parent.top,
                        bottom = balanceText.top,
                        start = parent.start,
                        end = payLayout.start,
                    )

                    // top.linkTo(parent.top)
                    // bottom.linkTo(balanceText.top)
                    // start.linkTo(parent.start)
                    // end.linkTo(payLayout.start)
                }, text = buildAnnotatedString {
                    pushStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold, fontSize = 16.sp
                        )
                    )
                    append("需充值")
                    pop()

                    pushStyle(
                        SpanStyle(
                            color = Color(0xFFFF6699),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    append(" 199 ")
                    pop()

                    pushStyle(
                        SpanStyle(
                            color = Color(0xFFFF6699),
                            fontSize = 16.sp,
                        )
                    )
                    append("B币")
                    pop()
                })

                Text(modifier = Modifier.constrainAs(balanceText) {
                    top.linkTo(chargeText.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(chargeText.start)
                }, text = buildAnnotatedString {
                    pushStyle(
                        SpanStyle(
                            fontSize = 13.sp, color = Color(0xFF9499A0)
                        )
                    )
                    append("账户余额抵扣100")
                    pop()

                    pushStyle(
                        SpanStyle(
                            fontSize = 13.sp, color = Color(0xFF9499A0)
                        )
                    )
                    append(" B币")
                    pop()
                })
            }
        }

        // 支付
        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp)
            .constrainAs(payLayout) { // linkTo(
                //     top = parent.top,
                //     bottom = parent.bottom,
                //     start = share.end,
                //     end = parent.end,
                //     horizontalBias = 1F
                // )
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(share.end)
                end.linkTo(parent.end)
            }
            .background(
                color = Color(0xFFFF6699), shape = RoundedCornerShape(37.dp)
            )
            .padding(10.dp), textAlign = TextAlign.Center, text = buildAnnotatedString {
            pushStyle(
                SpanStyle(
                    fontSize = 16.sp, color = Color.White
                )
            )
            append("领券购买")
            pop()

            pushStyle(
                SpanStyle(
                    color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold
                )
            )
            append(" 200 ")
            pop()

            pushStyle(
                SpanStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                )
            )
            append("B币")
            pop()
        })
    }
}


@Preview(name = "UniteBizBottomContainerV2")
@Composable
fun UniteBizBottomContainerV2(@PreviewParameter(CheeseBottomContainerProvider::class) data: OperationArea) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White), // horizontalArrangement = Arrangement.Center,//设置水平居中对齐
        verticalAlignment = Alignment.CenterVertically // 设置垂直居中对齐
    ) {
        // 收藏, 课堂群, 咨询, 课件, 分享, 充值, 支付
        val scope = rememberCoroutineScope()
        // 是否存在购买按钮, 存在的话: 购买按钮撑满剩余空间
        // 不存在: 组件均分
        val hasPurchaseBtn = true
        val itemModifier = if (!hasPurchaseBtn) Modifier.weight(1F) else Modifier

        // 收藏
        if (data.getFavorite()?.disabled == false) {
            val favoriteSelected = remember {
                mutableStateOf(data.getFavorite()?.favoriteSelected ?: false)
            }
            Column(
                modifier = itemModifier
                    .padding(start = ITEM_MARGIN_PADDING)
                    .size(40.dp)
                    .clickable {
                        scope.launch {
                            val result =
                                data.getFavorite()?.onClick?.invoke(data.getFavorite()) == true
                            if (result) {
                                data.getFavorite()?.favoriteSelected =
                                    data.getFavorite()?.favoriteSelected?.not() ?: false
                                favoriteSelected.value =
                                    data.getFavorite()?.favoriteSelected ?: false
                            }
                        }
                        // data.getFavorite()?.favoriteSelected = data.getFavorite()?.favoriteSelected?.not() ?: false
                        // favoriteSelected.value = data.getFavorite()?.favoriteSelected ?: false
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val drawable =
                    if (favoriteSelected.value) R.drawable.ic_comic_header else R.drawable.ic_multi_voice_setting
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = drawable),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier,
                    text = data.getFavorite()?.text ?: "",
                    fontSize = 10.sp,
                    color = ITEM_TEXT_COLOR
                )
            }
        }

        // 咨询
        if (data.getConsult()?.disabled == false) {
            Column(
                modifier = itemModifier
                    .padding(start = ITEM_MARGIN_PADDING)
                    .size(40.dp)
                    .clickable {

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_comic_header),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier,
                    text = data.getConsult()?.text ?: "",
                    fontSize = ITEM_TEXT_SIZE,
                    color = ITEM_TEXT_COLOR
                )
            }
        }


        // 分享
        if (data.getShare()?.disabled == false) {
            Column(
                modifier = itemModifier
                    .padding(start = ITEM_MARGIN_PADDING)
                    .size(40.dp)
                    .clickable {

                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_comic_header),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier,
                    text = data.getShare()?.text ?: "",
                    fontSize = ITEM_TEXT_SIZE,
                    color = ITEM_TEXT_COLOR
                )
            }
        }

        // 支付
        if (hasPurchaseBtn) {
            val backgroundColor =
                if (data.getPurchase()?.disabled == false) Color(0xFFFF6699) else Color.Gray
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(start = 10.dp, end = 10.dp)
                    .background(
                        color = backgroundColor, shape = RoundedCornerShape(37.dp)
                    )
                    .clickable {

                    },
                // box 内容居中
                contentAlignment = Alignment.Center
            ) {
                Text(modifier = Modifier.background(
                        color = backgroundColor, shape = RoundedCornerShape(37.dp)
                    ), textAlign = TextAlign.Center, text = buildAnnotatedString {
                    pushStyle(
                        SpanStyle(
                            fontSize = 16.sp, color = Color.White
                        )
                    )
                    append("领券购买")
                    pop()

                    pushStyle(
                        SpanStyle(
                            color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold
                        )
                    )
                    append(" 200 ")
                    pop()

                    pushStyle(
                        SpanStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    )
                    append("B币")
                    pop()
                })

            }
        }
    }
}

// @Preview(name = "TestView")
@Composable
fun TestView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(40.dp)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            text = "收藏",
            color = Color.Black,
            fontSize = 16.sp
        )

        Text(
            modifier = Modifier.size(40.dp),
            textAlign = TextAlign.Center,
            text = "咨询",
            color = Color.Black,
            fontSize = 16.sp
        )

        Text(
            modifier = Modifier.size(40.dp),
            textAlign = TextAlign.Center,
            text = "分享",
            color = Color.Black,
            fontSize = 16.sp
        )

        Text(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(end = 10.dp)
            .background(
                color = Color(0xFFFF6699), shape = RoundedCornerShape(37.dp)
            )
            .padding(10.dp), textAlign = TextAlign.Center, text = buildAnnotatedString {
            pushStyle(
                SpanStyle(
                    fontSize = 16.sp, color = Color.White
                )
            )
            append("领券购买")
            pop()

            pushStyle(
                SpanStyle(
                    color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold
                )
            )
            append(" 200 ")
            pop()

            pushStyle(
                SpanStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                )
            )
            append("B币")
            pop()
        })
    }
}

data class OperationArea(
    val list: List<OperationAreaButton>
) {
    private var mFavorite: OperationAreaButton? = null
    private var mGroup: OperationAreaButton? = null
    private var mCourseware: OperationAreaButton? = null
    private var mConsult: OperationAreaButton? = null
    private var mShare: OperationAreaButton? = null
    private var mPurchase: OperationAreaButton? = null

    init {
        list.forEach {
            when (it.type) {
                OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_FAVORITE -> mFavorite = it
                OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_GROUP -> mGroup = it
                OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_COURSEWARE -> mCourseware = it
                OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_CONSULT -> mConsult = it
                OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_SHARE -> mShare = it
                OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_PURCHASE -> mPurchase = it
                else -> mFavorite = it
            }
        }
    }

    fun resetFavorite(favorite: OperationAreaButton) {
        mFavorite = favorite
    }

    fun getFavorite() = mFavorite

    fun getGroup() = mGroup

    fun getCourseware() = mCourseware

    fun getConsult() = mConsult

    fun getShare() = mShare

    fun getPurchase() = mPurchase
}

fun getOperationAreaDefault() = OperationArea(list = ArrayList<OperationAreaButton>().apply {
    // 收藏
    add(
        OperationAreaButton(
            type = OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_FAVORITE,
            text = "收藏",
            disabled = false,
            link = "",
        )
    )
    // 群组
    add(
        OperationAreaButton(
            type = OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_GROUP,
            text = "群组",
            disabled = false,
            link = "",
        )
    )
    // 课件
    add(
        OperationAreaButton(
            type = OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_COURSEWARE,
            text = "课件",
            disabled = false,
            link = "",
        )
    )
    // 咨询
    add(
        OperationAreaButton(
            type = OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_CONSULT,
            text = "咨询",
            disabled = false,
            link = "https://www.google.com/",
        )
    )
    // 分享
    add(
        OperationAreaButton(
            type = OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_SHARE,
            text = "分享",
            disabled = false,
            link = "",
        )
    )
    // 购买
    add(
        OperationAreaButton(
            type = OperationAreaButton.OPERATION_AREA_BUTTON_TYPE_PURCHASE,
            text = "购买",
            disabled = false,
            link = "",
        )
    )
})


data class OperationAreaButton(
    val type: Int,
    val text: String,
    val disabled: Boolean,
    val link: String,

    var favoriteSelected: Boolean = false,
    var onClick: suspend (OperationAreaButton?) -> Boolean = { true }
) {
    companion object {
        // 未知
        const val OPERATION_AREA_BUTTON_TYPE_UNSPECIFIED = 0

        // 收藏
        const val OPERATION_AREA_BUTTON_TYPE_FAVORITE = 1

        // 群组
        const val OPERATION_AREA_BUTTON_TYPE_GROUP = 2

        // 课件
        const val OPERATION_AREA_BUTTON_TYPE_COURSEWARE = 3

        // 咨询
        const val OPERATION_AREA_BUTTON_TYPE_CONSULT = 4

        // 分享
        const val OPERATION_AREA_BUTTON_TYPE_SHARE = 5

        // 购买
        const val OPERATION_AREA_BUTTON_TYPE_PURCHASE = 6
    }
}

class CheeseBottomContainerProvider : PreviewParameterProvider<OperationArea> {
    private val operationArea = getOperationAreaDefault()
    override val values: Sequence<OperationArea>
        get() = listOf(operationArea).asSequence()
}