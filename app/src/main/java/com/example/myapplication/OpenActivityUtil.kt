package com.example.myapplication

import android.content.Context
import android.content.Intent


/**
 * 启动activity
 * openActivity<类>(context)
 */
inline fun <reified T> openActivity(context: Context) {
    val intent = Intent(context, T::class.java)
    context.startActivity(intent)
}

/**
 * 启动activity并且携带参数
 * openActivity<类>(context){ putExtra("key",value)}
 */
inline fun <reified T> openActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}