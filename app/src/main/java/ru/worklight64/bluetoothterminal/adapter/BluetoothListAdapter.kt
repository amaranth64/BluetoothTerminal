package ru.worklight64.bluetoothterminal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.worklight64.bluetoothterminal.R
import ru.worklight64.bluetoothterminal.databinding.BluetoothItemBinding
import ru.worklight64.bluetoothterminal.dataclass.BluetoothItem

class BluetoothListAdapter(private var listener: Listener): ListAdapter<BluetoothItem, BluetoothListAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = BluetoothItemBinding.bind(view)

        fun setData(item: BluetoothItem, listener: Listener) = with(binding){
            tvName.text = item.name
            tvMAC.text = item.mac

            itemView.setOnClickListener {
                listener.onClick(item)
            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_item, parent, false))
            }
        }

    }

    class ItemComparator: DiffUtil.ItemCallback<BluetoothItem>(){
        override fun areItemsTheSame(oldItem: BluetoothItem, newItem: BluetoothItem): Boolean {
            return oldItem.mac == newItem.mac
        }

        override fun areContentsTheSame(oldItem: BluetoothItem, newItem: BluetoothItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    interface Listener{
        fun onClick(item: BluetoothItem)
    }
}