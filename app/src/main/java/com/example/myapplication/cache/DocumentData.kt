package com.example.myapplication.cache

import android.net.Uri

data class DocumentData(val id:String, val size:Long, val name:String, val uri: Uri, val date:Long,
                        val type:String)
