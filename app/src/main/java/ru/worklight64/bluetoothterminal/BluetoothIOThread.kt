package ru.worklight64.bluetoothterminal

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class BluetoothIOThread(bluetoothSocket: BluetoothSocket, private val receiveDataListener: ReceiveDataListener): Thread() {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    init {
        try {
            inputStream = bluetoothSocket.inputStream
        }catch (i: IOException){

        }

        try {
            outputStream = bluetoothSocket.outputStream
        }catch (i: IOException){

        }
    }

    override fun run() {
        val buffer = ByteArray(256)
        while (true){
            try {
                val size = inputStream?.read(buffer)
                val message = String(buffer, 0, size!!)
                Log.d("MyLog", "Message: $message")
                receiveDataListener.onBluetoothReceive(message)
            } catch (i: IOException){

            }
        }
    }

    fun sendMessage(byteArray: ByteArray){
        try {
            outputStream?.write(byteArray)
        }catch (i: IOException){

        }
    }

    interface ReceiveDataListener{
        fun onBluetoothReceive(data: String)
    }

}