package com.example.iamhome

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val deviceName: TextView = itemView.findViewById(R.id.recyclerView)
    val deviceStatus: TextView = itemView.findViewById(R.id.recyclerView)
    val deviceImage: ImageView = itemView.findViewById(R.id.recyclerView)

}