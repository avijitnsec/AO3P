package com.medtronic.ao3p

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class WifiDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_detail)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text
        val textView = findViewById<TextView>(R.id.textView).apply {
            text = message
        }

        val networkPass = findViewById<EditText>(R.id.password)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener(View.OnClickListener {
            connectToWifi("Theater of Dreams", "Arijit@12")
//            val conf = WifiConfiguration()
//            conf.SSID =
//                "\"" + "Theater of Dreams" + "\"" // Please note the quotes. String should contain ssid in quotes
//            conf.preSharedKey = "\"" + "Arijit@12" + "\"";
////            conf.preSharedKey = "\""+ networkPass.text.toString() +"\"";
//            //conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//
//            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//
//            val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//            val ret = wm.addNetwork(conf)
//            wm.disconnect()
//            wm.enableNetwork(ret, true)
//            wm.reconnect()
        })


    }

    fun getWiFiConfig(ssid: String): WifiConfiguration? {
        val wm:WifiManager= applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiList=wm.configuredNetworks
        for (item in wifiList){
            if(item.SSID != null && item.SSID == ssid){
                return item
            }
        }
        return null
    }

    fun connectToWifi(ssid: String, password: String) {
        val connManager = applicationContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        val wifiConfiguration = WifiConfiguration()
        wifiConfiguration.SSID = String.format("\"%s\"", ssid)
        wifiConfiguration.preSharedKey = String.format("\"%s\"", password)
        var wifiManager = applicationContext!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val netId: Int = wifiManager.addNetwork(wifiConfiguration)
        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()
        val config = WifiConfiguration()
        config.SSID = "\"$ssid\"";
        config.preSharedKey = "\"\"" + password + "\"\""
        wifiManager.addNetwork(config)
    }

    private fun connectWifi(ssid: String, keyP: String) {
//        var wifiConfig = WifiConfiguration()
        var wifiConfig = getWiFiConfig(ssid)
     //   wifiConfig!!.SSID = "\"$ssid\"";
//        wifiConfig!!.preSharedKey  = "\"$keyP\"";
//        wifiConfig.SSID = java.lang.String.format("\"%s\"", ssid)
//        wifiConfig.preSharedKey = String.format("\"%s\"", keyP)

        wifiConfig!!.wepTxKeyIndex = 0;
//        wifiConfig.hiddenSSID = true
        wifiConfig!!.priority = 1;
        wifiConfig!!.status = WifiConfiguration.Status.ENABLED;
//        wifiConfig.status = WifiConfiguration.Status.ENABLED
        wifiConfig!!.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
        wifiConfig!!.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
        wifiConfig!!.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
        wifiConfig!!.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
        wifiConfig!!.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
        wifiConfig!!.allowedProtocols.set(WifiConfiguration.Protocol.RSN)

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var netId = wifiManager.addNetwork(wifiConfig)

        if (netId == -1){
            //Try it again with no quotes in case of hex password
            wifiConfig.wepKeys[0] = keyP;
            netId = wifiManager.addNetwork(wifiConfig);
        }
        Log.d("Avijit", "$netId")
        val enable = wifiManager.enableNetwork(netId, true)
        Log.d("WifiPreference", "enableNetwork returned $enable")

        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()
    }
}