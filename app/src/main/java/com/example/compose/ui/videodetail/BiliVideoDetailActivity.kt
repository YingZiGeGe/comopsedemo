package com.example.compose.ui.videodetail

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.example.compose.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-06-10
 * @desc   :
 *
 */
class BiliVideoDetailActivity : BaseActivity() {
    private val dataFlow = MutableStateFlow<OperationArea?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = getOperationAreaDefault().apply {
            this.getFavorite()?.onClick = {
                Log.d(TAG, "Cheese.Ship onCreate favorite click 000 = $it")
                delay(1_000)
                true
            }
        }
        dataFlow.value = data

        setContent {
            UniteBizBottomContainerV2(dataFlow.collectAsState().value?.apply {
                this.getFavorite()?.onClick = {
                    Log.d(TAG, "Cheese.Ship onCreate favorite click")
                    delay(200)
                    val result = Random.nextBoolean()
                    Log.d(TAG, "Cheese.Ship onCreate favorite click result = $result")
                    true
                }
            } ?: getOperationAreaDefault().apply {
                this.getFavorite()?.onClick = {
                    Log.d(TAG, "Cheese.Ship onCreate default favorite click 002 = $it")
                    true
                }
            })
        }

        // GlobalScope.launch {
        //     dataFlow.collectLatest {
        //         Log.d(TAG, "Cheese.Ship dataFlow collect = $it")
        //     }
        //
        //     delay(3_000)
        //     dataFlow.value = getOperationAreaDefault().apply {
        //         this.getFavorite()?.favoriteSelected = false
        //     }
        // }
    }

    companion object {
        private const val TAG = "BiliVideoDetailActivity"
    }
}