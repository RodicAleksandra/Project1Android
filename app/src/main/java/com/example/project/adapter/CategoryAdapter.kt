package com.example.project.adapter

import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.example.project.data.Category
import com.example.project.databinding.HolderCategoryBinding
import com.example.project.holder.CategoryHolder

class CategoryAdapter(val categories: ArrayList<Category>, val imgLoader: ImageLoader): RecyclerView.Adapter<CategoryHolder>()
{

    lateinit var categoryClickListener: (Category, Int) -> Unit

    fun setOnCategoryClickListener(listener: (Category, Int) -> Unit) {
        categoryClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HolderCategoryBinding.inflate(layoutInflater, parent, false)
        return CategoryHolder(binding)

    }


    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {

        holder.bind(categories[position], imgLoader)

        if(this::categoryClickListener.isInitialized) {
            holder.itemView.setOnClickListener {
                categoryClickListener(categories[position], position)
            }
        }
    }


    override fun getItemCount()= categories.size

}
