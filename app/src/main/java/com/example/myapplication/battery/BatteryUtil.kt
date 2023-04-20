package com.example.myapplication.battery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.BatteryState
import android.os.BatteryManager
import android.util.Log

/**
 * 获取电池信息
 */
object BatteryUtil {
    private const val TAG = "BatteryUtil"
    private var technology: String? = null
    private var voltage = 0
    private var level = 0
    private var temperature = 0.0
    private var status: String? = null
    private var health: String? = null
    private var plugged: String? = null



    /**
     * 获取电池容量
     */
    @SuppressLint("PrivateApi")
    fun getBatteryCapacity(context: Context): String {
        val mPowerProfile: Any
        var batteryCapacity = 0.0
        val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"
        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                .getConstructor(Context::class.java)
                .newInstance(context)
            batteryCapacity = Class
                .forName(POWER_PROFILE_CLASS)
                .getMethod("getBatteryCapacity")
                .invoke(mPowerProfile) as Double
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return batteryCapacity.toString()
    }

    /**
     * 通过接收系统广播获取电池的信息
     *       mLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    mScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    mHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
    mPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
    mPresent = intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
    mStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
    mTechnology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
    mTemperature = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
    mVoltage = ((float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)) / 1000;
     */
    fun receiverBatteryOtherInfo(context: Context) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val receiver = context.registerReceiver(null, filter)

        if (receiver != null) {
            technology = receiver.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            Log.d(TAG, "电池信息 technology:$technology")
        } //获取电池技术
        if (technology == "" || technology == null) {
            technology = "Unknown"
        }
        if (receiver != null) {
            voltage = receiver.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
            Log.d(TAG, "电池信息 voltage:$voltage")
        } //获取电压(mv)
        if (receiver != null) {
            level = receiver.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = receiver.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level=  (level * 100 / scale.toFloat()).toInt()
            Log.d(TAG, "电池信息 level:$level")
        } //获取当前电量
        if (receiver != null) {
            temperature = receiver.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0
            Log.d(TAG, "电池信息 temperature:$temperature")
        } //获取温度(数值)并转为电池摄氏温度
        if (receiver != null) {
            when (receiver.getIntExtra(BatteryManager.EXTRA_STATUS, 0)) {
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> status = "未知"
                BatteryManager.BATTERY_STATUS_CHARGING -> status = "充电中"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> status = "放电中"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> status = "未充电"
                BatteryManager.BATTERY_STATUS_FULL -> {
                    status = "电池满"
                    //存储断开充电器电池充满的时间
                }
            }
        }
        Log.d(TAG, "电池信息 status:$status")
        if (receiver != null) {
            when (receiver.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)) {
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> health = "未知"
                BatteryManager.BATTERY_HEALTH_GOOD -> health = "良好"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> health = "过热"
                BatteryManager.BATTERY_HEALTH_DEAD -> health = "没电"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> health = "过电压"
                BatteryManager.BATTERY_HEALTH_COLD -> health = "温度过低"
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> health = "未知错误"
            }
        }
        Log.d(TAG, "health:$health")
        if (receiver != null) {
            plugged = when (receiver.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)) {
                BatteryManager.BATTERY_PLUGGED_AC -> "充电器"
                BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> "无线充电"
                else -> "未充电"
            }
        }
        Log.d(TAG, "电池信息 plugged:$plugged")
    }
}
