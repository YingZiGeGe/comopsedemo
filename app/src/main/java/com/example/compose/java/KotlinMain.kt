package com.example.compose.java

/**
 *
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time   : 2023-12-07
 * @desc   :
 *
 */
fun main() {
    val gInteger = Generic<Int>(10086)
    val gNumber = Generic<Number>(456)

    showKeyValue(gInteger)
    showKeyValue(gNumber)
    Class.forName("")
}

fun showKeyValue(obj: Generic<*>) {
    println("泛型测试: key value is: ${obj.getKey()}")
}

class Generic<T>(private val key: T) {

    fun getKey(): T {
        return key
    }

    @Throws(InstantiationException::class, IllegalAccessException::class)
    fun <T : Any> genericMethod(tClass: Class<T>): T {
        return tClass.newInstance()
    }

    companion object {
        fun <T> show(t: T) {

        }
    }
}

