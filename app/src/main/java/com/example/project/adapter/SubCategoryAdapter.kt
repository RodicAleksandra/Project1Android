package com.example.project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.example.project.data.SubCategory
import com.example.project.databinding.HolderSubcategoryBinding
import com.example.project.holder.SubCategoryHolder

class SubCategoryAdapter(val subcategoryList: ArrayList<SubCategory>, val imgLoader: ImageLoader): RecyclerView.Adapter<SubCategoryHolder>() {

    lateinit var subcategoryClickListener: (SubCategory, Int) -> Unit

    fun setOnSubcategoryClickListener(listener: (SubCategory, Int) -> Unit) {
        subcategoryClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HolderSubcategoryBinding.inflate(layoutInflater, parent, false)
        return SubCategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryHolder, position: Int) {
        holder.bind(subcategoryList[position], imgLoader)

        if (this::subcategoryClickListener.isInitialized) {
            holder.itemView.setOnClickListener {
                subcategoryClickListener(subcategoryList[position], position)
            }
        }
    }

    override fun getItemCount() = subcategoryList.size
}