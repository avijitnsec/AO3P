package com.medtronic.ao3p

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast


class WifiReceiver: BroadcastReceiver() {

    private var wifiManager: WifiManager? = null
    private var sb: java.lang.StringBuilder? = null
    private var wifiDeviceList: ListView? = null

    override fun onReceive(p0: Context?, p1: Intent?) {

    }

}
