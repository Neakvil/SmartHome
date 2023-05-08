package com.example.iamhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        //val adapter = MyAdapter(devicesList)
        //recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}