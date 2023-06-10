package com.example.iamhome.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iamhome.R
import com.example.iamhome.model.Device

class DeviceAdapter(
    private val dataset: List<Device>
    ): RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

        inner class DeviceViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
            val nameTextView: TextView = view.findViewById(R.id.device_name_textView)
            val typeTextView: TextView = view.findViewById(R.id.device_type_textView)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
       val adapterLayout = LayoutInflater.from(parent.context)
           .inflate(R.layout.device_item, parent, false)

        Log.i("DeviceAdapter", "Start")
        return DeviceViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val item = dataset[position]

        holder.nameTextView.text = item.name
        Log.i("DeviceAdapter", "${item.name}")
        holder.typeTextView.text = item.type
    }
}