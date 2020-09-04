package com.medtronic.ao3p

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var wifiList = ArrayList<String>()
    private var wifiManager: WifiManager? = null
    private val MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1
    var receiverWifi: WifiReceiver? = null
    var wifiDeviceList: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiDeviceList = findViewById(R.id.wifiList)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)

        val scanButton = findViewById<Button>(R.id.scanBtn)
        scanButton.setOnClickListener(View.OnClickListener {
            wifiManager!!.startScan()
            if (!wifiManager!!.isWifiEnabled) {
                Toast.makeText(applicationContext, "Network Disabled", Toast.LENGTH_SHORT).show()
            }

            for (data in wifiManager!!.scanResults) {

                //Log.d("avijit", "type -> ${typeOf(data)}")
                wifiList!!.add(data.SSID)
            }

            val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                applicationContext,
                android.R.layout.simple_list_item_1,
                wifiList.toArray()
            )
            wifiDeviceList!!.adapter = arrayAdapter

            wifiDeviceList!!.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
                    Log.d("Avijit", "$i - $l - ${wifiList[i]}")
                    val deviceName = wifiList[i]
                    val intent = Intent(this, WifiDetailActivity::class.java).apply {
                        putExtra(EXTRA_MESSAGE, deviceName)
                    }
                    startActivity(intent)
                }
        })


    }

    private fun scanFailure() {
        //TODO("Not yet implemented")
    }

    private fun scanSuccess() {
        for (d in wifiList)
            Log.d("Avijit", "$d")


    }
}