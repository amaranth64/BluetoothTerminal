package ru.worklight64.bluetoothterminal

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.worklight64.bluetoothterminal.databinding.ActivityMainBinding
import ru.worklight64.bluetoothterminal.dataclass.BluetoothItem


class MainActivity : AppCompatActivity(), BluetoothIOThread.ReceiveDataListener {
    private lateinit var form: ActivityMainBinding
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    lateinit var bluetoothConnection: BluetoothConnection
    private var listItem: BluetoothItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        form = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(form.root)

        onBluetoothListResult()

        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        bluetoothConnection = BluetoothConnection(btAdapter, this)

        form.bMsgLedOn.setOnClickListener {
            bluetoothConnection.sendMessage("*L:1;")
        }


        form.bMsgLedOff.setOnClickListener {
            bluetoothConnection.sendMessage("*L:0;")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.control_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_connect){
            listItem.let {
                bluetoothConnection.connect(it?.mac!!)
            }
        } else if (item.itemId == R.id.id_list){
            activityLauncher.launch(Intent(this, BluetoothListActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onBluetoothListResult(){
        activityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                listItem = it.data?.getSerializableExtra(BluetoothListActivity.DEVICE_KEY) as BluetoothItem
            }
        }
    }

    override fun onBluetoothReceive(data: String) {
        runOnUiThread {
            form.tvEmpty.append(data)
        }

    }

}