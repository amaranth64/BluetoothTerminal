package ru.worklight64.bluetoothterminal

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class ConnectionThread(private val device: BluetoothDevice): Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var mSocket: BluetoothSocket? = null
    lateinit var ioThread: BluetoothIOThread
    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        }catch (i: IOException){

        }
    }

    override fun run() {
        try {
            Log.d("MyLog","Connecting...")
            mSocket?.connect()
            Log.d("MyLog","Connected")
            ioThread = BluetoothIOThread(mSocket!!)
            ioThread.start()
        }catch (i: IOException){
            Log.d("MyLog","Can not connect to device")
            closeConnection()
        }
    }

    fun closeConnection(){
        try {
            mSocket?.close()
        }catch (i: IOException){

        }
    }
}