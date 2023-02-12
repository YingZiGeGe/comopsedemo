package com.example.compose.ui.multivoicemanager

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-02-05
 * @desc   :
 *
 */
class LiveMulVoiceManagerViewModel: ViewModel() {
    var managerUIState = MutableLiveData(LiveMultiVoiceManagerUIState(
        applyList = ArrayList<LiveMultiVoiceApplyItem>().apply {
            repeat(50) {
                add(LiveMultiVoiceApplyItem().apply {
                    uid = it.toLong()
                    header = R.drawable.ic_comic_header
                    name = "Simon $it"
                    time = "01-12 11:57"
                    acceptEvent = {
                        acceptApply(this)
                    }
                    refuseEvent = {
                        refuseApply(this)
                    }
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
                    inviteEvent = {
                        invite(this)
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
                    viewModelScope.launch {
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
    )
    var managerUIState0 = MutableStateFlow(null)
    // private var managerUIState = MutableSharedFlow<LiveMultiVoiceManagerUIState>()
    init {

    }

    private suspend fun acceptApply(item: LiveMultiVoiceApplyItem): Boolean {
        Log.d("ManagerVM", "Simon.Debug acceptApply 000 = ${item.name}")
        delay(200)
        val random = Random.nextBoolean()
        Log.d("ManagerVM", "Simon.Debug acceptApply 001 = $random")
        return true
    }

    private suspend fun refuseApply(item: LiveMultiVoiceApplyItem): Boolean {
        Log.d("ManagerVM", "Simon.Debug refuseApply 000 = ${item.name}")
        delay(200)
        val random = Random.nextBoolean()
        Log.d("ManagerVM", "Simon.Debug refuseApply 001 = $random")
        return true
    }

    private fun invite(item: LiveMultiVoiceInviteItem) {
        Log.d("ManagerVM", "Simon.Debug invite 000 = ${item.name}")
        viewModelScope.launch {
            managerUIState.value = managerUIState.value.apply {
                this?.inviteList?.forEach {
                    if (it.uid == item.uid) {
                        Log.d("ManagerVM", "Simon.Debug invite 001 = ${item.name}")
                        item.inviteState = LiveMultiVoiceInviteItem.STATE_INVITED
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "LiveManagerViewModel"
    }
}