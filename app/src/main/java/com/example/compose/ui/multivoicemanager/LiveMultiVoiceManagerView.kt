package com.example.compose.ui.multivoicemanager

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.compose.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-01-08
 * @desc   :
 *
 */

private const val TAG = "MultiVoiceManager"

// @Preview(name = "LiveMultiVoiceManagerView")
@ExperimentalPagerApi
@Composable
private fun LiveMultiVoiceManagerView(@PreviewParameter(LiveMultiVoiceProvider::class) data: LiveMultiVoiceManagerUIState) {
    val titles = remember {
        data.titles
    }
    val pageState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF1F2022),
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            )
        // .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            // 定义 tabRow 和 设置图标 id
            val (tabRow, icSetting) = createRefs()

            // 申请列表, 邀请列表
            ScrollableTabRow(
                modifier = Modifier
                    .constrainAs(tabRow) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .wrapContentWidth()
                    .fillMaxHeight(),
                // ScrollableTabRow 默认带有 backgroundColor, 无法使用 modifier background 覆盖
                backgroundColor = Color.Transparent,
                selectedTabIndex = pageState.currentPage,
                // 控制 TabRow 左右 padding
                edgePadding = 15.dp,
                indicator = { positions ->
                    // 设置滑动条的属性, 默认是白色的
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(positions[pageState.currentPage])
                            .height(3.dp)
                            .padding(start = 30.dp, end = 30.dp)
                            .clip(shape = RoundedCornerShape(6.dp)),
                        color = Color(0xFFFF6699)
                    )
                },
                divider = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    ) {}
                }
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = index == pageState.currentPage,
                        onClick = {
                            scope.launch {
                                Log.d(TAG, "tab click scrollToPage = $index")
                                pageState.scrollToPage(index)
                            }
                        },
                        // 如果已选中, 再次点击不做响应
                        enabled = index != pageState.currentPage
                    ) {
                        // 为了让 indicator 和 title 有间距, 这里设置 box 的 height 即可达到效果
                        Box(
                            modifier = Modifier.height(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val color =
                                if (index == pageState.currentPage) Color.White else Color(
                                    0xCCFFFFFF
                                )
                            Text(
                                // modifier = Modifier.height(30.dp),
                                text = title,
                                color = color,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // 设置按钮
            Box(
                modifier = Modifier
                    .constrainAs(icSetting) {
                        top.linkTo(tabRow.top)
                        bottom.linkTo(tabRow.bottom)
                        end.linkTo(parent.end, margin = 10.dp)
                    }
                    // .padding(end = 10.dp)
                    .wrapContentWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_multi_voice_setting),
                    contentDescription = null,
                    // colorFilter = ColorFilter.tint(color = Color.Red)
                )
            }
        }

        HorizontalPager(
            count = titles.size,
            state = pageState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            // page = 0: 申请列表, page = 1: 邀请列表
            when (page) {
                0 -> {
                    // todo Simon.Debug 首次加载两个列表时, 进行请求
                    LaunchedEffect(key1 = "test") {

                    }
                    // todo Simon.Debug 可能需要取消?
                    // DisposableEffect(key1 = "test")
                    LiveMultiVoiceApplyList(data)
                }
                1 -> {
                    LiveMultiVoiceInviteList(data)
                }
            }
        }
    }
}

// 申请列表
@Preview(name = "ApplyList")
@Composable
private fun LiveMultiVoiceApplyList(@PreviewParameter(LiveMultiVoiceProvider::class) data: LiveMultiVoiceManagerUIState) {
    if (data.applyList?.isNotEmpty() == true) {
        // 数据不为空时, 展示底部最后一条~
        (data.applyList as? ArrayList)?.add(LiveMultiVoiceApplyItem())
        val listState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState
        ) {
            // todo Simon.Debug 数据进来前需要先过滤下重复数据, 不然 compose 会 crash
            // todo Simon.Debug 因为 item 标识必须唯一, 不然 compose 会 crash? 待确认
            try {
                itemsIndexed(
                    items = data.applyList,
                    // 默认情况下列表项的状态是和位置绑定的，倘若数据集发生更改，
                    // 那么状态就会丢失，从而可能出现数据混乱的情况。
                    // 所以这里需要使用Key来进行状态保存
                    key = { _, item ->
                        item.hashCode()
                    }) { index, item ->

                    if (index == data.applyList.size - 1) {
                        // 最后一条数据
                        LiveMultiVoiceLastItemView()
                    } else {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            val (header, name, time, refuse, accept) = createRefs()

                            // 头像
                            // todo Simon.Debug 需要改为网络加载
                            Image(
                                modifier = Modifier
                                    .constrainAs(header) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start, margin = 15.dp)
                                    }
                                    .size(35.dp)
                                    .clip(CircleShape),
                                painter = painterResource(id = R.drawable.ic_comic_header),
                                contentDescription = null)

                            // 创建 name 和 time 的 垂直 chain
                            createVerticalChain(name, time, chainStyle = ChainStyle.Packed)
                            // 名字
                            Text(
                                modifier = Modifier
                                    .constrainAs(name) {
                                        top.linkTo(header.top)
                                        start.linkTo(header.end, margin = 8.dp)
                                        bottom.linkTo(time.top)
                                    },
                                text = item.name ?: "",
                                fontSize = 13.sp,
                                color = Color.White
                            )

                            // 申请时间
                            Text(
                                modifier = Modifier
                                    .constrainAs(time) {
                                        top.linkTo(name.bottom)
                                        bottom.linkTo(header.bottom)
                                        start.linkTo(name.start)
                                    },
                                text = item.time ?: "",
                                fontSize = 10.sp,
                                color = Color(0xFFC9CCD0)
                            )

                            // 接收按钮
                            Text(
                                modifier = Modifier
                                    .constrainAs(accept) {
                                        top.linkTo(header.top)
                                        bottom.linkTo(header.bottom)
                                        end.linkTo(parent.end, margin = 15.dp)
                                    }
                                    .background(
                                        color = Color(0xFFFF6699),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 3.dp,
                                        bottom = 3.dp
                                    ),
                                text = "接收",
                                color = Color.White,
                                fontSize = 13.sp)

                            // 拒绝按钮
                            Text(
                                modifier = Modifier
                                    .constrainAs(refuse) {
                                        // 这里直接使用 linkTo 就可以使用 bias 啦~
                                        linkTo(
                                            top = header.top,
                                            bottom = header.bottom,
                                            start = name.end,
                                            end = accept.start,
                                            endMargin = 8.dp,
                                            horizontalBias = 1F
                                        )
                                    }
                                    // 不填充使用 border!
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFFFF6699),
                                                Color(0xFFFF6699)
                                            )
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 3.dp,
                                        bottom = 3.dp
                                    ),
                                text = "拒绝",
                                color = Color(0xFFFF6699),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Simon.Debug", "Simon.Debug apply mic list error = $e")
            }
        }

    } else {
        LiveMultiVoiceApplyListEmpty()
    }
}

// 申请列表空视图
// @Preview(name = "ApplyListEmpty")
@Composable
private fun LiveMultiVoiceApplyListEmpty() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (emptyView, emptyText, btnInvite) = createRefs()
        // 创建垂直约束 chain, 使 view 整体上下居中
        createVerticalChain(emptyView, emptyText, btnInvite, chainStyle = ChainStyle.Packed)

        // 空视图
        Image(
            modifier = Modifier
                .constrainAs(emptyView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(emptyText.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(144.dp)
                .height(82.dp),
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = null)

        // 空文案
        Text(
            modifier = Modifier.constrainAs(emptyText) {
                top.linkTo(emptyView.bottom)
                bottom.linkTo(btnInvite.top)
                start.linkTo(emptyView.start)
                end.linkTo(emptyView.end)
            },
            text = stringResource(id = R.string.live_multi_voice_apply_list_empty),
            color = Color(0xFF6F6F6F),
            fontSize = 14.sp
        )

        // 邀请按钮
        Text(
            modifier = Modifier
                // 这里要注意使用 chain 后, margin 不生效, 只能通过设置 modifier 的 padding(紧跟 modifier 时就是 margin)
                .padding(top = 16.dp)
                .constrainAs(btnInvite) {
                    top.linkTo(emptyText.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(emptyView.start)
                    end.linkTo(emptyView.end)
                }
                .background(color = Color(0xFFFF6699), RoundedCornerShape(4.dp))
                .padding(
                    start = 23.dp,
                    end = 23.dp,
                    top = 6.dp,
                    bottom = 6.dp
                ),
            text = "去邀请",
            color = Color.White,
            fontSize = 14.sp)
    }
}

// 邀请列表
// todo Simon.Debug 正在查看为什么 invite list 无法预览, 但 apply list 可以预览
@Preview(name = "InviteList")
@Composable
private fun LiveMultiVoiceInviteList(@PreviewParameter(LiveMultiVoiceProvider::class) data: LiveMultiVoiceManagerUIState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (data.inviteList?.isNotEmpty() == true) {
            (data.inviteList as? ArrayList)?.add(LiveMultiVoiceInviteItem())

            Text(
                modifier = Modifier
                    .padding(start = 15.dp),
                text = "房间内观众",
                color = Color(0xFF9499A0),
                fontSize = 12.sp
            )

            // 邀请 list
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(items = data.inviteList,
                    key = { _, item ->
                        item.hashCode()
                    }) { index, item ->
                    if (index == data.inviteList.size - 1) {
                        LiveMultiVoiceLastItemView()
                    } else {
                        ConstraintLayout(modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)) {
                            val (header, name, interactionValue, btnInvite) = createRefs()

                            // todo Simon.Debug 需要改为网络加载
                            Image(
                                modifier = Modifier
                                    .constrainAs(header) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start, margin = 15.dp)
                                    }
                                    .size(35.dp)
                                    .clip(CircleShape),
                                painter = painterResource(id = R.drawable.ic_comic_header),
                                contentDescription = null)

                            createVerticalChain(name, interactionValue, chainStyle = ChainStyle.Packed)
                            // 名字
                            Text(
                                modifier = Modifier
                                    .constrainAs(name) {
                                        top.linkTo(header.top)
                                        bottom.linkTo(interactionValue.top)
                                        start.linkTo(header.end, margin = 8.dp)
                                    },
                                text = item.name ?: "",
                                fontSize = 13.sp,
                                color = Color.White
                            )

                            // 互动值
                            Text(
                                modifier = Modifier
                                    .constrainAs(interactionValue) {
                                        top.linkTo(name.bottom)
                                        bottom.linkTo(header.bottom)
                                        start.linkTo(name.start)
                                    },
                                text = item.interactionValue?.toString() ?: "",
                                fontSize = 10.sp,
                                color = Color(0xFFC9CCD0)
                            )

                            // 邀请按钮
                            val textInvite =
                                if (item.inviteState == LiveMultiVoiceInviteItem.STATE_NONE) "邀请连麦" else "已邀请"
                            val colorInvite =
                                if (item.inviteState == LiveMultiVoiceInviteItem.STATE_NONE) Color(
                                    0xFFFF6699
                                ) else Color(0x66FF6699)
                            Text(modifier = Modifier
                                .constrainAs(btnInvite) {
                                    top.linkTo(header.top)
                                    bottom.linkTo(header.bottom)
                                    end.linkTo(parent.end, margin = 15.dp)
                                }
                                .width(66.dp)
                                .height(24.dp)
                                .border(
                                    width = 1.dp,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            colorInvite,
                                            colorInvite
                                        )
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            text = textInvite,
                            color = colorInvite)
                        }
                    }
                }
            }
        } else {
            LiveMultiVoiceInviteListEmpty()
        }
    }
}

// 邀请列表空视图
// @Preview(name = "InviteListEmpty")
@Composable
private fun LiveMultiVoiceInviteListEmpty() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (emptyView, emptyText, btnInvite) = createRefs()
        // 创建垂直约束 chain, 使 view 整体上下居中
        createVerticalChain(emptyView, emptyText, chainStyle = ChainStyle.Packed)

        // 空视图
        Image(
            modifier = Modifier
                .constrainAs(emptyView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(emptyText.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(144.dp)
                .height(82.dp),
            painter = painterResource(id = R.drawable.ic_empty_view),
            contentDescription = null)

        // 空文案
        Text(
            modifier = Modifier
                .padding(top = 4.dp)
                .constrainAs(emptyText) {
                top.linkTo(emptyView.bottom)
                bottom.linkTo(btnInvite.top)
                start.linkTo(emptyView.start)
                end.linkTo(emptyView.end)
            },
            text = stringResource(id = R.string.live_multi_voice_invite_list_empty),
            color = Color(0xFF6F6F6F),
            fontSize = 14.sp,
            // 居中
            textAlign = TextAlign.Center
        )
    }
}

// 最后一条数据
// @Preview(name = "LastItemView")
@Composable
private fun LiveMultiVoiceLastItemView() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        val (bottomText) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(bottomText) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        start = parent.start,
                        end = parent.end,
                        verticalBias = 0.55F
                    )
                },
            text = "已经到底啦~",
            fontSize = 12.sp,
            color = Color(0xFF757A81)
        )
    }
}
