package com.example.myapplication.image

import android.location.Location
import android.media.ExifInterface
import android.util.Log
import java.io.IOException

/**
 * 修改照片信息
 */
object ModifyExif {
    private var exif: ExifInterface? = null


    //设置exif
    fun setExif(filepath: String, longitude: Double, latitude: Double, time: String) {
        try {
            exif = ExifInterface(filepath) //根据图片的路径获取图片的Exif
        } catch (ex: IOException) {
            Log.e("ModifyExif", "cannot read exif", ex)
        }
        exif?.setAttribute(ExifInterface.TAG_GPS_LATITUDE, gpsInfoConvert(latitude)) //把纬度写进exif
        exif?.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, if (latitude > 0) "N" else "S")
        exif?.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, if (longitude > 0) "E" else "W")
        exif?.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, gpsInfoConvert(longitude))
        exif?.setAttribute(ExifInterface.TAG_DATETIME, time) //把时间写进exif
//        exif?.setAttribute(ExifInterface.TAG_MAKE, longitude) //把经度写进MAKE 设备的制造商，当然这样也是可以的，大家都是Stirng类型
//        exif?.setAttribute(ExifInterface.TAG_MODEL, latitude) //把纬度写进MODEL

        try {
            exif?.saveAttributes() //最后保存起来
            Log.i("ModifyExif", "照片信息   保存成功")
        } catch (e: IOException) {
            Log.e("ModifyExif", "cannot save exif", e)
        }
    }

    //获取exif
    fun getExif(filepath: String): ExifInterface? {
        try {
            exif = ExifInterface(filepath)
          val dateTime = exif?.getAttribute(ExifInterface.TAG_DATETIME)
            val longitude = exif?.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            val latitude = exif?.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
        Log.i("ModifyExif", "照片信息：  dateTime: $dateTime   longitude:  $longitude  latitude:  $latitude")
        //想要获取相应的值：exif.getAttribute("对应的key")；比如获取时间：exif.getAttribute(ExifInterface.TAG_DATETIME);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return exif
    }

    fun gpsInfoConvert(gpsInfo: Double): String {
        var gpsInfo = gpsInfo
        gpsInfo = Math.abs(gpsInfo)
        val dms: String = Location.convert(gpsInfo, Location.FORMAT_SECONDS)
        val splits = dms.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val seconds = (java.lang.Double.valueOf(splits[2]) * 1000).toInt().toDouble()
        return splits[0] + "/1," + splits[1] + "/1," + seconds + "/1000"
    }
}
