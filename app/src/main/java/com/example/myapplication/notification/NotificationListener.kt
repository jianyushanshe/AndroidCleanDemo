package com.example.myapplication.notification

import android.app.Notification
import android.graphics.Bitmap
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi


/**
 * https://blog.csdn.net/lyz_zyx/article/details/65440927
 *
StatusBarNotification[] sbns = getActiveNotifications();         // 返回当前系统所有通知的数组
cancelAllNotifications(); //移除所有可移除的通知
cancelNotification(String key); //移除指定key的通知，要求api21以上
cancelNotifications(String[] keys); //移除指定数组内的所有key的通知，要求api21以上
getActiveNotifications()； //获取通知栏上的所有通知，返回一个StatusBarNotification[]


tatusBarNotification
sbn.getId();                               // 返回通知对应的id
sbn.getNotification();                          // 返回通知对象
sbn.getPackageName();                          // 返回通知对应的包名
sbn.getPostTime();                            // 返回通知发起的时间
sbn.getTag();                              // 返回通知的Tag，如果没有设置返回null
sbn.isClearable();                            // 返回该通知是否可被清楚，是否为FLAG_ONGOING_EVENT、FLAG_NO_CLEAR
sbn.isOngoing();                             // 返回该通知是否在正在运行的，是否为FLAG_ONGOING_EVENT

Notification notification = sbn.getNotification();
notification.contentView;                        // 通知的RemoteViews
notification.contentIntent;                       // 通知的PendingIntent
notification.actions;                          // 通知的行为数组
// Android4.4后还扩展了可以获取通知详情信息
if (Build.VERSION.SDK_INT >Build.VERSION_CODES.JELLY_BEAN_MR2) {
Bundle extras = notification.extras;
String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
int notificationIcon = extras.getInt(Notification.EXTRA_SMALL_ICON);
Bitmap notificationLargeIcon = ((Bitmap)extras.getParcelable(Notification.EXTRA_LARGE_ICON));
CharSequence notificationText = extras.getCharSequence(Notification.EXTRA_TEXT);
CharSequence notificationSubText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
}


 通知监听服务，在清单文件中注册后，系统会自动启动，不用手动操作
 */
class NotificationListener : NotificationListenerService() {
    override fun onListenerConnected() {
        //当连接成功时调用，一般在开启监听后会回调一次该方法
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        //当移除一条消息的时候回调，sbn是被移除的消息

    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        //当收到一条消息时回调，sbn里面带有这条消息的具体信息
        val packageName = sbn?.packageName//获取发送通知的应用程序包名
        val isClearable = sbn?.isClearable//通知是否可被清除
        val id  =  sbn?.id //获取通知id
        val key = sbn?.key//获取通知的key
        val postTime = sbn?.postTime//通知的发送时间


        val notification = sbn?.notification//获取Notification
        val extras = sbn!!.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE) //通知title
        val content = extras.getString(Notification.EXTRA_TEXT) //通知内容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            val smallIconId = extras.getInt(Notification.EXTRA_SMALL_ICON) //通知小图标id
            val largeIcon = extras.getParcelable<Bitmap>(Notification.EXTRA_LARGE_ICON) //通知的大图标，注意和获取小图标的区别
        }else{
            val smallIconId = sbn.notification.smallIcon //通知小图标id
            val largeIcon = sbn.notification.getLargeIcon() //通知的大图标，注意和获取小图标的区别
        }
        val pendingIntent = sbn.notification.contentIntent //获取通知的PendingIntent

        if (packageName=="com.appsinnova.android.keepclean"){//清理特定应用的通知
            cancelAllNotifications()
            Log.i("NotificationManager","通知消息管理   清理通知:packageName:$packageName   isClearable:$isClearable    id:$id   key:$key   postTime:$postTime   title:$title   content:$content")
        }
        cancelAllNotifications()

        Log.i("NotificationManager","通知消息管理   收到通知:packageName:$packageName   isClearable:$isClearable    id:$id   key:$key   postTime:$postTime   title:$title   content:$content")
    }
}