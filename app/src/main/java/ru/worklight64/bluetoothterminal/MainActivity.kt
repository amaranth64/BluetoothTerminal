package ru.worklight64.bluetoothterminal

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.worklight64.bluetoothterminal.adapter.BluetoothListAdapter
import ru.worklight64.bluetoothterminal.databinding.ActivityMainBinding
import ru.worklight64.bluetoothterminal.dataclass.BluetoothItem


class MainActivity : AppCompatActivity() {
    private lateinit var form: ActivityMainBinding
    private var btAdapter: BluetoothAdapter? = null
    private lateinit var adapter: BluetoothListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        form = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(form.root)
        adapter = BluetoothListAdapter()
        form.rcBluetoothDevice.layoutManager = LinearLayoutManager(this)
        form.rcBluetoothDevice.adapter = adapter
        initializeBluetooth()
    }


    private fun initializeBluetooth() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            val requiredPermissions = listOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
            val missingPermissions = requiredPermissions.filter { permission ->
                checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
            }

            if (missingPermissions.isNotEmpty()) {
                requestPermissions(missingPermissions.toTypedArray(), BLUETOOTH_PERMISSION_REQUEST_CODE)
                return
            }
        }


        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter

        if (btAdapter?.isEnabled == false) {
            val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(turnOn)
        }

        val pairedDevices: Set<BluetoothDevice>? = btAdapter?.bondedDevices
        val tempList = ArrayList<BluetoothItem>()
        pairedDevices?.forEach{
            form.tvEmpty.append(it.name)
            tempList.add(BluetoothItem(it.name, it.address))
        }
        if (tempList.isNotEmpty()) form.tvEmpty.visibility = View.GONE
        adapter.submitList(tempList)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            BLUETOOTH_PERMISSION_REQUEST_CODE -> {
                if (grantResults.none { it != PackageManager.PERMISSION_GRANTED }) {
                    // all permissions are granted
                    initializeBluetooth()
                } else {
                    finish()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        private const val BLUETOOTH_PERMISSION_REQUEST_CODE = 9999
    }

}