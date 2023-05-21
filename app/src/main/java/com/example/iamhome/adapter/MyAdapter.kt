package com.example.iamhome.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iamhome.MyViewHolder
import com.example.iamhome.R
import com.example.iamhome.data.Device
import com.squareup.picasso.Picasso

class MyAdapter(
    private val context: Context,
    private val devices: List<Device>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val device = devices[position]
        holder.deviceName.text = device.name
        holder.deviceStatus.text = device.status

        Picasso.get()
            .load(device.imageUrl)  // Замените device.imageUrl на ссылку на изображение в вашей модели данных
            .placeholder(R.drawable.background)  // Замените placeholder на ресурс-заглушку по вашему выбору
            .error(R.drawable.images)  // Замените error_image на ресурс-изображение для отображения при ошибке загрузки
            .into(holder.deviceImage)
    }

    override fun getItemCount(): Int = devices.size
}