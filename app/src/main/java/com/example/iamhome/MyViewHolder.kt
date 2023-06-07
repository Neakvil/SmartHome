package com.example.iamhome

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val deviceName: TextView = itemView.findViewById(R.id.recyclerViewDevices)
    val deviceStatus: TextView = itemView.findViewById(R.id.recyclerViewDevices)
    val deviceImage: ImageView = itemView.findViewById(R.id.recyclerViewDevices)

}