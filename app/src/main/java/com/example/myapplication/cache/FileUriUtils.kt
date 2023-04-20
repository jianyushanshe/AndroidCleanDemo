package com.example.myapplication.cache

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile


object FileUriUtils {
    var root: String = Environment.getExternalStorageDirectory().path + "/"
    fun treeToPath(path: String): String {
        var path2: String  =""
        if (path.contains("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary")) {
            path2 = path.replace(
                "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A",
                root
            )
            path2 = path2.replace("%2F", "/")
        } else {
           // path2 = root + getSubString(path + "测试", "document/primary%3A", "测试").replace("%2F", "/")
        }
        return path2
    }


    //直接返回DocumentFile
    fun getDocumentFilePath(context: Context, path: String, sdCardUri: String?): DocumentFile? {
        var document = DocumentFile.fromTreeUri(context, Uri.parse(sdCardUri))
        val parts = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (i in 3 until parts.size) {
            document = document!!.findFile(parts[i])
        }
        return document
    }

    //转换至uriTree的路径
     fun changeToUri(path: String): String {
        var path = path
        if (path.endsWith("/")) {
            path = path.substring(0, path.length - 1)
        }
        val path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
        return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path2"
    }


    //根据路径获得document文件
    fun getDocumentFileFromPath(context: Context?, path: String): DocumentFile? {
        var path = path
        if (path.endsWith("/")) {
            path = path.substring(0, path.length - 1)
        }
        val path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
        return DocumentFile.fromSingleUri(
            context!!,
            Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path2")
        )
    }


    //转换至uriTree的路径
    fun changeToUri2(path: String): String {
        val paths =
            path.replace("/storage/emulated/0/Android/data".toRegex(), "").split("/".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val stringBuilder =
            StringBuilder("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata")
        for (p: String in paths) {
            if (p.length == 0) continue
            stringBuilder.append("%2F").append(p)
        }
        return stringBuilder.toString()
    }
    fun getDocumentFile(context: Context,path:String)=DocumentFile.fromTreeUri(context, Uri.parse(
        changeToUri3(path)
    ))
//changeToUri3方法是我封装好的方法，后面会用到，这个是通过path生成指定可解析URI的方法

    //转换至uriTree的路径
    fun changeToUri3(path: String): String {
        var path = path
        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
        return "content://com.android.externalstorage.documents/tree/primary%3A$path"
    }


    //判断是否已经获取了Data权限，改改逻辑就能判断其他目录，懂得都懂
     fun  isGrant(context:Context):Boolean {
        for ( persistedUriPermission in context.contentResolver.persistedUriPermissions) {
            if (persistedUriPermission.isReadPermission && persistedUriPermission.uri.toString() == "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata") {
                return true
            }
        }
        return false
    }


    //获取指定目录的权限
    fun startFor(path: String, context: Activity, REQUEST_CODE_FOR_DIR: Int) {
        val uri = changeToUri(path)
        val parse: Uri = Uri.parse(uri)
        val intent = Intent("android.intent.action.OPEN_DOCUMENT_TREE")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse)
        }
        context.startActivityForResult(intent, REQUEST_CODE_FOR_DIR)
    }

    //直接获取data权限，推荐使用这种方案
    fun startForRoot(context: Activity, REQUEST_CODE_FOR_DIR: Int) {
        val uri1: Uri =
            Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata")
        //        DocumentFile documentFile = DocumentFile.fromTreeUri(context, uri1);
        var uri = changeToUri(Environment.getExternalStorageDirectory().path)
        uri = "$uri/document/primary%3A" + Environment.getExternalStorageDirectory().path
            .replace("/storage/emulated/0/", "").replace("/", "%2F")
        val parse: Uri = Uri.parse(uri)
        val documentFile = DocumentFile.fromTreeUri(context, uri1)
        val intent1 = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent1.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
        intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile!!.uri)
        context.startActivityForResult(intent1, REQUEST_CODE_FOR_DIR)
    }
}

