package com.example.myapplication.cache

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.*
import java.util.*

/**
 *    author : fySpring
 *    date   : 2021/3/18 7:00 PM
 *    desc   : 针对Android R 的适配，实现访问 Android/data 目录
 */
object CleanRDataUtil {

         val TAG = "ClearMemoryActivity"
         const val EXTERNAL_STORAGE_PROVIDER_AUTHORITY =
            "com.android.externalstorage.documents"
         const val ANDROID_DOCUMENT_ID = "primary:Android"
        //如果你需要访问 obb 目录，把 data 改成 obb 即可
         const val ANDROID_DATA_DOCUMENT_ID = "primary:Android/data"
         const val ANDROID_OBB_DOCUMENT_ID = "primary:Android/obb"

         val androidDataTreeUri = DocumentsContract.buildTreeDocumentUri(
            EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
            ANDROID_DATA_DOCUMENT_ID
        )

        // Android/data 目录 uri
         val androidChildDataTreeUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            androidDataTreeUri,
            ANDROID_DATA_DOCUMENT_ID
        )

        //获取 data目录下所有文件夹uri
        @SuppressLint("Range")
        fun getAndroidDataUri(contentResolver: ContentResolver): MutableMap<String, Uri> {
            val packageMap: MutableMap<String, Uri> = mutableMapOf()
            val cursor = contentResolver.query(
                androidChildDataTreeUri,
                arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_MIME_TYPE), null, null, null)
            cursor?.let {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                    val id = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                    val type = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                    if (type == DocumentsContract.Document.MIME_TYPE_DIR) {
                        packageMap[name] = DocumentsContract.buildChildDocumentsUriUsingTree(
                            androidChildDataTreeUri, id)
                    }
                }
            }
            cursor?.close()
            return packageMap
        }

        //获取URI下 cache 目录，替换名称也可以获取其他目录
        @SuppressLint("Range")
        fun getAndroidDataCacheUri(contentResolver: ContentResolver, uri: Uri): Uri? {
            var result: Uri? = null
            val cursor = contentResolver.query(uri,
                arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME, DocumentsContract.Document.COLUMN_DOCUMENT_ID, DocumentsContract.Document.COLUMN_MIME_TYPE),
                null, null, null)
            cursor?.let {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                    val type = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                    if (type == DocumentsContract.Document.MIME_TYPE_DIR && name.lowercase(Locale.ROOT) == "cache") {
                        val id = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                        result = DocumentsContract.buildChildDocumentsUriUsingTree(uri, id)
                    }
                }
            }
            cursor?.close()
            return result
        }

        //获取URI下 cache 目录，替换名称也可以获取其他目录

        @SuppressLint("Range")
        fun getAndroidDataCacheUri(contentResolver: ContentResolver, uri: Uri, fileName:String): Uri? {
            var result: Uri? = null
            val cursor = contentResolver.query(uri,
                arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME, DocumentsContract.Document.COLUMN_DOCUMENT_ID, DocumentsContract.Document.COLUMN_MIME_TYPE),
                null, null, null)
            cursor?.let {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                    val type = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                    if (type == DocumentsContract.Document.MIME_TYPE_DIR && name.lowercase(Locale.ROOT) == fileName) {
                        val id = it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                        result = DocumentsContract.buildChildDocumentsUriUsingTree(uri, id)
                    }
                }
            }
            cursor?.close()
            return result
        }

        /**
         * 扫描获取所有文件
         */
        @SuppressLint("Range")
        fun scanFile(contentResolver: ContentResolver, uri: Uri): MutableList<DocumentData> {
            val documentList = mutableListOf<DocumentData>()
            val cursor = contentResolver.query(
                uri,
                arrayOf(
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_SIZE,
                    DocumentsContract.Document.COLUMN_LAST_MODIFIED
                ),
                null, null, null
            )
            cursor?.let {
                while (it.moveToNext()) {
                    val name =
                        it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                    val type =
                        it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                    val id =
                        it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                    val size = it.getLong(it.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE))
                    val date =
                        it.getLong(it.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED))
                    if (type == DocumentsContract.Document.MIME_TYPE_DIR) {
                        documentList.addAll(scanFile(contentResolver, DocumentsContract.buildChildDocumentsUriUsingTree(uri, id)))
                    } else {
                        documentList.add(DocumentData(id, size, name, uri, date, type))
                    }
                }
            }

            cursor?.close()
            return documentList
        }

        /**
         * 遍历获取所有文件的大小
         */
        @SuppressLint("Range")
        fun scanFileForSize(contentResolver: ContentResolver, uri: Uri): Long {
            var totalSize = 0L
            val cursor = contentResolver.query(
                uri,
                arrayOf(
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_SIZE
                ),
                null, null, null
            )
            cursor?.let {
                while (it.moveToNext()) {
                    val name =
                        it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                    val type =
                        it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))
                    val id =
                        it.getString(it.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))
                    val size = it.getLong(it.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE))
                    if (type == DocumentsContract.Document.MIME_TYPE_DIR) {
                        totalSize += scanFileForSize(
                            contentResolver,
                            DocumentsContract.buildChildDocumentsUriUsingTree(uri, id)
                        )
                    } else {
                        totalSize += size
                    }
                }
            }

            cursor?.close()
            return totalSize
        }

        /**
         * 删除文件
         */
        fun deleteCurDocument(context: Context, uri: Uri) :Boolean{
            return try {
                DocumentsContract.deleteDocument(context.contentResolver, uri)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        /**
         * 直接获取 data 权限
         */
        fun startForDataPermission(activity: Activity, code: Int) {
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
                        Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                putExtra(
                    DocumentsContract.EXTRA_INITIAL_URI,
                    DocumentFile.fromTreeUri(activity, androidDataTreeUri)?.uri
                )
            }.also {
                activity.startActivityForResult(it, code)
            }
        }


        /**
         * 判断是否已经获取了 data 权限
         */
        fun isDataGrant(context: Context): Boolean {
            for (persistedUriPermission in context.contentResolver.persistedUriPermissions) {
                if ((persistedUriPermission.uri == androidDataTreeUri) &&
                    persistedUriPermission.isWritePermission &&
                    persistedUriPermission.isReadPermission
                ) {
                    return true
                }
            }
            return false
        }


     var listMap = mutableMapOf<String, FileArray>()
     var allSize = 0L
     val asyncList = mutableListOf<Deferred<Any>>()
    /**
     * Android 11 扫描data目录  所有应用的缓存cache
     */
     suspend fun startScanDataAboveR(context: Context) {
        allSize = 0L
        listMap.clear()
        withContext(Dispatchers.IO) {
            val packageNameList = getAndroidDataUri(context.contentResolver)
            for ((name, uri) in packageNameList) {
                asyncList.add(async {
                    if (!name.startsWith(".")) {
                        //扫描到的应用
//                        val data = AppCleanData(name, getIcon(name), getName(name), "", 0L)
//                        delay(50L)
//                        addApp.postValue(data)
//                        appList.add(data)
                        Log.i(TAG, "缓存扫描到的应用   : $name")
                        val cacheUri =
                            getAndroidDataCacheUri(context.contentResolver, uri)
                        if (cacheUri != null) {
                            val size = scanFileForSize(context.contentResolver, cacheUri)
                            val item = StorageDataBean()
                            item.name = name
                            item.uri = cacheUri
                            item.isFolder = false
                            item.length = size
                            Log.i(TAG, "缓存扫描到的应用   : $name   $size")
                            if (listMap[name] == null) {
                                listMap[name] = FileArray()
                            }
                            listMap[name]?.let {
                                with(it) {
                                    this.size += size
                                    this.storageDataList.add(item)
                                }
                            }

                            allSize += size
                            //allSizeLiveData.postValue(allSize.byteToFitSizeForStorageArray())
                        }
                    }
                })
            }
            asyncList.awaitAll()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "扫描缓存结束总大小：$allSize", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * 获取目标应用中的目标文件夹
     * targetPackageName 目标应用包名
     * folder            文件夹名称
     * fileName          文件名称
     */
     fun getTargetFolder(context: Context, targetPackageName: String, folderName: String): DocumentFile? {
        val uri = createAppDataDirUri(targetPackageName)
        val documentFile = DocumentFile.fromTreeUri(context, uri) ?: return null
        return documentFile.findFile(folderName)
    }
    /**
     * 获取目标应用中的目标文件夹Uri
     * targetPackageName 目标应用包名
     * folder            文件夹名称
     * fileName          文件名称
     */
    fun getTargetFolderUri(context: Context, targetPackageName: String, folderName: String): Uri? {
        val uri = createAppDataDirUri(targetPackageName)
        val documentFile = DocumentFile.fromTreeUri(context, uri) ?: return null
        return documentFile.findFile(folderName)?.uri
    }
    /**
     * 删除目标应用中的目标文件夹
     * targetPackageName 目标应用包名
     * folder            文件夹名称
     * fileName          文件名称
     */
     fun deleteTargetFolder(context: Context,targetPackageName:String,folderName:String):Boolean{
        val uri = createAppDataDirUri(targetPackageName)//根据packageName生成应用包名文件夹uri
        val documentFile = DocumentFile.fromTreeUri(context, uri)//获取应用文件夹下的文件列表
        val filesDocumentFile = documentFile?.findFile(folderName)//查找到对应的文件夹目录
        return filesDocumentFile?.delete() == true//删除对应的目录s
    }
    /**
     * 获取目标应用中的目标文件
     * targetPackageName 目标应用包名
     * folder            文件夹名称
     * fileName          文件名称
     */
     fun getTargetFile(context: Context,targetPackageName:String,folderName:String,fileName: String): DocumentFile? {
        val uri = createAppDataDirUri(targetPackageName)
        val documentFile = DocumentFile.fromTreeUri(context, uri) ?: return null
        val filesDocumentFile = documentFile.findFile(folderName) ?: return null
        return filesDocumentFile.findFile(fileName)
    }
    /**
     * 删除目标应用中的目标文件
     * targetPackageName 目标应用包名
     * folder            文件夹名称
     * fileName          文件名称
     */
     fun deleteTargetFile(context: Context,targetPackageName:String,folderName:String,fileName: String): Boolean{
        val uri = createAppDataDirUri(targetPackageName)
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        val filesDocumentFile = documentFile?.findFile(folderName)
        return filesDocumentFile?.findFile(fileName)?.delete() ?: false
    }
    /**
     * 创建指定应用的目标目录dir
     */
    fun createAppDataDirUri(packageName: String) =
        Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2F${packageName}")!!

    /**
     * 是否可以访问指定应用的data目录
     */
     fun canReadDataDir(context: Context,packageName: String)=
        DocumentFile.fromTreeUri(context, createAppDataDirUri(packageName))?.canRead() ?: false

}