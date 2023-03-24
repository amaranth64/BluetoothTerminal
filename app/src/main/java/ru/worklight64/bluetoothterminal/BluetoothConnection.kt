package ru.worklight64.bluetoothterminal

import android.bluetooth.BluetoothAdapter

class BluetoothConnection(private val adapter: BluetoothAdapter) {
    lateinit var cThread: ConnectionThread
    fun connect(mac:String){
        if (adapter.isEnabled and mac.isNotEmpty()){
            val device = adapter.getRemoteDevice(mac)
            device.let{
                cThread = ConnectionThread(it)
                cThread.start()
            }
        }
    }

    fun sendMessage(mgs: String){
        cThread.ioThread.sendMessage(mgs.toByteArray())
    }

}