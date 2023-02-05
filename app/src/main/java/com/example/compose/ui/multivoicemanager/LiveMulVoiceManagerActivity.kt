@file:OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)

package com.example.compose.ui.multivoicemanager

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.compose.BaseActivity
import com.example.compose.R
import com.example.compose.ui.theme.ComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-01-14
 * @desc   :
 *
 */
class LiveMulVoiceManagerActivity: BaseActivity() {
    private var managerUIState = LiveMultiVoiceManagerUIState(
        applyList = ArrayList<LiveMultiVoiceApplyItem>().apply {
            repeat(50) {
                add(LiveMultiVoiceApplyItem().apply {
                    uid = it.toLong()
                    header = R.drawable.ic_comic_header
                    name = "Simon $it"
                    time = "01-12 11:57"
                })
            }
        },
        inviteList = ArrayList<LiveMultiVoiceInviteItem>().apply {
            repeat(50) {
                add(LiveMultiVoiceInviteItem().apply {
                    uid = it.toLong()
                    header = R.drawable.ic_comic_header
                    name = "Simon $it"
                    interactionValue = 7070
                    inviteState = if (it == 1) {
                        LiveMultiVoiceInviteItem.STATE_INVITED
                    } else {
                        LiveMultiVoiceInviteItem.STATE_NONE
                    }
                })
            }
        },
        currentPage = LiveMultiVoiceManagerUIState.PAGE_APPLY_LIST,
        firstLoad = { currentPage ->
            Log.d(TAG, "Simon.Debug currentPage = $currentPage")
            when (currentPage) {
                // todo Simon.Debug 准备开始写初始化加载逻辑
                LiveMultiVoiceManagerUIState.PAGE_APPLY_LIST -> {
                    lifecycleScope.launch {
                        // 模拟网络加载
                        delay(200)
                    }
                }
                LiveMultiVoiceManagerUIState.PAGE_INVITE_LIST -> {

                }
                else -> {
                    Log.e(TAG, "Simon.Debug error page = $currentPage")
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManagerMainView()
            // LiveMultiVoiceManagerView(managerUIState)
        }
    }

    @Composable
    private fun ManagerMainView() {
        val scope = rememberCoroutineScope()
        val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        // 弹起管理员面板按钮
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        // 这里的弹框弹出需要通过数据变更进行展示, 不能直接在 clickable 中嵌套 compose view
                        scope.launch {
                            if (sheetState.isVisible) sheetState.hide() else sheetState.show()
                        }
                    },
                text = "弹起管理员面板")
        }

        ManagerDialog(sheetState)
    }

    // @Preview(name = "ManagerDialog")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ManagerDialog(sheetState: ModalBottomSheetState, dismiss: () -> Unit = {}) {
        // ModalBottomSheetLayout/BottomSheetScaffold 代替了 java 中的 BottomSheetDialogFragment
        ModalBottomSheetLayout(
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        // height 大约为屏幕高度的一半
                        // todo Simon.Debug 怎样固定高度, 不让可滑动高度...目前高度可滑动两次..才能 dismiss
                        .height(LocalConfiguration.current.screenHeightDp.dp * 0.66F)
                ) {
                    LiveMultiVoiceManagerView(managerUIState)
                }
            },
        modifier = Modifier,
        sheetState = sheetState) {
            // empty content
        }
    }

    companion object {
        private const val TAG = "LiveMulVoiceManager"
    }
}

@Preview(name = "ManagerPreview")
@Composable
fun ManagerPreview() {
    ComposeTheme {
        LiveMultiVoiceManagerView(LiveMultiVoiceManagerUIState())
    }
}

@ExperimentalMaterialApi
@Preview(name = "bottomSheetScaffoldTest")
@Composable
fun BottomSheetScaffoldTest() {
    val  scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        sheetContent = {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = " sheetContent title")
            Text(text = " sheetContent content")
            Text(text = " sheetContent content2")
            Text(text = " sheetContent content3")
            Text(text = " sheetContent content4")
            Text(text = " sheetContent content5")
            Text(text = " sheetContent content6")
            Text(text = " sheetContent content7")
            Text(text = " sheetContent content8")
            Text(text = " sheetContent content9")
            Spacer(modifier = Modifier.height(10.dp))
        },
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        topBar = { topBarView(scaffoldState) },
        //        snackbarHost =
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        if(scaffoldState.bottomSheetState.isCollapsed){
                            scaffoldState.bottomSheetState.expand()
                        }else{
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            ){

            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        sheetGesturesEnabled = true,
        sheetShape = RoundedCornerShape(2.dp),
        sheetElevation = 8.dp,
        sheetBackgroundColor = Color.Red,
        sheetContentColor = Color.White,
        sheetPeekHeight = 50.dp,
        drawerContent = {
            drawView()
        },
        drawerGesturesEnabled = true,
        drawerShape = RoundedCornerShape(4.dp),
        drawerElevation = 16.dp,
        drawerBackgroundColor = Color.White,
        drawerContentColor = Color.Black,
        drawerScrimColor = DrawerDefaults.scrimColor,
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "  BottomSheetScaffold  title")
        Text(text = "  BottomSheetScaffold  content")
        Button(onClick = {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("被打开了","隐藏",SnackbarDuration.Short)
            }
        }) {
            Text(text = "打开SnackBar")
        }
    }
}

@Composable
fun drawView(){
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalDrawer(
        drawerContent = {
            Text(text = "ModalDrawer Content",modifier = Modifier
                .fillMaxWidth()
                .height(200.dp))
        },
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerShape = MaterialTheme.shapes.large,
        drawerElevation = DrawerDefaults.Elevation,
        drawerBackgroundColor = Color.Red,
        drawerContentColor = Color.White,
        scrimColor = DrawerDefaults.scrimColor
    ) {
        Column() {
            Text(text = "ModalDrawer Title")
            Text(text = "ModalDrawer Content")
            Button(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Text(text = "打开")
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun topBarView(scaffoldState:BottomSheetScaffoldState){
    val scope = rememberCoroutineScope()
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        backgroundColor = MaterialTheme.colors.primarySurface,
        elevation = 4.dp,
        contentPadding = AppBarDefaults.ContentPadding
    ) {
        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "返回按钮",
                tint = Color.White,
                modifier = Modifier
                    .clickable {
                        // 打开drawer
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                    .padding(start = 12.dp, end = 12.dp)
                    .fillMaxHeight()
            )
            Text(text = "页面标题",fontSize = 17.sp,color = Color.White)
        }
    }
}


@ExperimentalMaterialApi
@Preview(name = "modalBottomSheetLayoutTest")
@Composable
fun ModalBottomSheetLayoutTest(){
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetContent = {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = " sheetContent title")
            Text(text = " sheetContent content")
            Text(text = " sheetContent content2")
            Text(text = " sheetContent content3")
            Text(text = " sheetContent content4")
            Text(text = " sheetContent content5")
            Text(text = " sheetContent content6")
            Text(text = " sheetContent content7")
            Text(text = " sheetContent content8")
            Text(text = " sheetContent content9")
            Spacer(modifier = Modifier.height(10.dp))
        },
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(4.dp),
        sheetElevation = 16.dp,
        sheetBackgroundColor = Color.Red,
        sheetContentColor = Color.White,
        scrimColor = ModalBottomSheetDefaults.scrimColor)
    {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = " ModalBottomSheetLayout title")
            Text(text = " ModalBottomSheetLayout content")
            Button(onClick = {
                scope.launch {
                    if(sheetState.isVisible) sheetState.hide() else sheetState.show()
                }
            }) {
                Text(text = " 打开")
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}