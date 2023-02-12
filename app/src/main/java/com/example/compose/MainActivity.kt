package com.example.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.ui.multivoicemanager.LiveMulVoiceManagerActivity
import com.example.compose.ui.multivoicemanager.Test
import com.example.compose.ui.theme.ComposeTheme

class MainActivity : BaseActivity() {
    private val items = listOf<Class<*>>(
        LiveMulVoiceManagerActivity::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                MainFunctionList(this@MainActivity)
            }
        }

        // testFun(int = 0, true)
        testFun(int = 0, booleanArrayOf(true))
        testFun2()
    }

    private fun testFun(int: Int, vararg test: Any) {
        Log.d("testFun", "testFun 000 int = $int, test = $test")
        Log.d("testFun", "testFun 001 int = $int, test = ${test.firstOrNull()}")
        Log.d("testFun", "testFun 002 int = $int, test = ${(test.firstOrNull() as? Array<*>)?.firstOrNull()}")
    }

    private fun testFun2() {
        // val test = Test.IEventCenter().sendEvent(1, booleanArrayOf(true))
        val test = Test.IEventCenter().sendEvent(1, true)
        Log.d("testFun1", "testFun1 000 test = $test")
        Log.d("testFun1", "testFun1 001 test = ${(test as? Array<*>)?.firstOrNull()}")
        Log.d("testFun1", "testFun1 002 test = ${((test as? Array<*>)?.firstOrNull() as? Array<*>)?.firstOrNull()}")
    }

    // @Preview(name = "MainFunctionList")
    @Composable
    private fun MainFunctionList(context: Context) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            itemsIndexed(items = items) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            // startActivity(Intent(LocalContext.current, item))
                            startActivity(Intent(context, item))
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = "$index ${item.simpleName}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(name = "testLazyColumn")
@Composable
private fun TestLazyColumn() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                text = "HEADER"
            )
        }

        // items(count = 20) {
        //     Text(
        //         modifier = Modifier
        //             .fillMaxWidth()
        //             .height(60.dp),
        //         text = "POS = $it"
        //     )
        // }

        val list = mutableListOf<String>().apply {
            repeat(20) {
                add(it.toString())
            }
        }
        itemsIndexed(items = list) { index, item ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                text = "POS = $item"
            )
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                text = "FOOTER"
            )
        }
    }
}