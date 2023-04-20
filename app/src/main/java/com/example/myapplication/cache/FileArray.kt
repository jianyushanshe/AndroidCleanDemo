package com.example.myapplication.cache

import java.io.File


data class FileArray(
    val fileList: MutableList<File> = mutableListOf(),
    val storageDataList: MutableList<StorageDataBean> = mutableListOf(),
    var size: Long = 0L
)

