package com.example.compose.ui.multivoicemanager

import androidx.annotation.DrawableRes
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.compose.R

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-01-11
 * @desc   : 语聊房管理员管理页面(邀请列表, 申请列表)数据
 *
 */
data class LiveMultiVoiceManagerUIState(
    val titles: MutableList<String> = mutableListOf("申请消息", "邀请列表"),
    val applyList: List<LiveMultiVoiceApplyItem>? = null,
    val inviteList: List<LiveMultiVoiceInviteItem>? = null,
    var currentPage: Int = PAGE_APPLY_LIST,
    val firstLoad: (Int) -> Unit = {}
) {
    companion object {
        // 申请列表
        const val PAGE_APPLY_LIST = 0

        // 邀请列表
        const val PAGE_INVITE_LIST = 1
    }
}

// 申请列表数据
data class LiveMultiVoiceApplyItem(
    var uid: Long? = null,
    @DrawableRes var header: Int? = null,
    var name: String? = null,
    var time: String? = null
)

// 邀请列表数据
data class LiveMultiVoiceInviteItem(
    var uid: Long? = null,
    @DrawableRes var header: Int? = null,
    var name: String? = null,
    var interactionValue: Int? = null,
    // 0 未邀请, 1 已邀请
    var inviteState: Int = STATE_NONE
) {
    companion object {
        const val STATE_NONE = 0
        const val STATE_INVITED = 1
    }
}

class LiveMultiVoiceProvider : PreviewParameterProvider<LiveMultiVoiceManagerUIState> {
    private val applyList = ArrayList<LiveMultiVoiceApplyItem>().apply {
        repeat(100) {
            add(LiveMultiVoiceApplyItem().apply {
                uid = it.toLong()
                header = R.drawable.ic_comic_header
                name = "Simon $it"
                time = "01-12 11:57"
            })
        }
    }
    private val inviteList = ArrayList<LiveMultiVoiceInviteItem>().apply {
        repeat(100) {
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
    }

    private val manager0 =
        LiveMultiVoiceManagerUIState(
            applyList = applyList,
            inviteList = inviteList
        )

    private val manager1 =
        LiveMultiVoiceManagerUIState(
            applyList = null,
            inviteList = inviteList
        )

    private val manager2 =
        LiveMultiVoiceManagerUIState(
            applyList = applyList,
            inviteList = null
        )

    private val manager3 =
        LiveMultiVoiceManagerUIState(
            applyList = null,
            inviteList = null
        )

    override val values: Sequence<LiveMultiVoiceManagerUIState>
        get() = listOf(manager0/*, manager1, manager2, manager3*/).asSequence()
}