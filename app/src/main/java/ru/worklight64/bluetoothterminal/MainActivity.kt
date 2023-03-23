package ru.worklight64.bluetoothterminal

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.worklight64.bluetoothterminal.databinding.ActivityMainBinding
import ru.worklight64.bluetoothterminal.dataclass.BluetoothItem


class MainActivity : AppCompatActivity() {
    private lateinit var form: ActivityMainBinding
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        form = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(form.root)

        onBluetoothListResult()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.control_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_connect){
            activityLauncher.launch(Intent(this, BluetoothListActivity::class.java))
        } else if (item.itemId == R.id.id_list){

        }

        return super.onOptionsItemSelected(item)
    }

    private fun onBluetoothListResult(){
        activityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                form.tvEmpty.text = (it.data?.getSerializableExtra(BluetoothListActivity.DEVICE_KEY) as BluetoothItem).name
            }
        }
    }

}