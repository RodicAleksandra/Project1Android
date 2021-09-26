package com.example.project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.data.Order
import com.example.project.databinding.HolderOrderBinding
import com.example.project.holder.OrderHolder

class OrderAdapter(val orderList: ArrayList<Order>): RecyclerView.Adapter<OrderHolder>() {

    lateinit var binding: HolderOrderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HolderOrderBinding.inflate(layoutInflater, parent, false)
        return OrderHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount() = orderList.count()
}