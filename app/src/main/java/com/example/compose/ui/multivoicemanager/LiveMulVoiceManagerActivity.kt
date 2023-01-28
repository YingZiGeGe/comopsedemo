@file:OptIn(ExperimentalPagerApi::class)

package com.example.compose.ui.multivoicemanager

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.compose.BaseActivity
import com.example.compose.ui.theme.ComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi

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
        firstLoad = { currentPage ->
            Log.d(TAG, "Simon.Debug currentPage = $currentPage")
            when (currentPage) {
                LiveMultiVoiceManagerUIState.PAGE_APPLY_LIST -> {

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
        val showDialog = remember { mutableStateOf(false) }
        // 弹起管理员面板按钮
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        // 这里的弹框弹出需要通过数据变更进行展示, 不能直接在 clickable 中嵌套 compose view
                        showDialog.value = true
                    },
                text = "弹起管理员面板")
        }

        if (showDialog.value) {
            ManagerDialog {
                showDialog.value = false
            }
        }
    }

    // @Preview(name = "ManagerDialog")
    @Composable
    private fun ManagerDialog(dismiss: () -> Unit) {
        Dialog(onDismissRequest = {
            Log.d(TAG, "Simon.Debug dialog dismiss")
            dismiss.invoke()
        }) {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
                LiveMultiVoiceManagerView(LiveMultiVoiceManagerUIState())
            }
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