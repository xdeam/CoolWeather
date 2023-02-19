package com.coolweather.android.util

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object FileUtils {

    fun getJson(fileName: String, context: Context) :String{
        //将json数据变成字符串

        //将json数据变成字符串
        val stringBuilder = StringBuilder()
        try {
            //获取assets资源管理器
            val assetManager: AssetManager = context.getAssets()
            //通过管理器打开文件并读取
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}