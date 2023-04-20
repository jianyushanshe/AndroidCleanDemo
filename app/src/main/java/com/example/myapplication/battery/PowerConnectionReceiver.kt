package com.example.myapplication.battery

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PowerConnectionReceiver :BroadcastReceiver(){
    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            if (intent?.action==Intent.ACTION_POWER_DISCONNECTED)
            BatteryUtil.receiverBatteryOtherInfo(context)
        }
    }
}