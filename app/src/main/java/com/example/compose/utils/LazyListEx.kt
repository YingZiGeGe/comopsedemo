package com.example.compose.utils

import androidx.compose.foundation.lazy.LazyListState

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-01-11
 * @desc   :
 *
 */

/**
 * list 中最后一条数据是否在展示中
 */
fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1