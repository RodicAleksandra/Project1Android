package com.example.project.holder

import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.example.project.R
import com.example.project.data.Category
import com.example.project.databinding.HolderCategoryBinding


class CategoryHolder(val binding: HolderCategoryBinding): RecyclerView.ViewHolder(binding.root) {





    fun bind(category: Category, imageLoader: ImageLoader) {
        binding.tvCategory.text = category.strCategoryName
        binding.tvCategoryDesc.text = category.strCategoryDesc
        imageLoader.get(
            "https://rjtmobile.com/grocery/images/${category.strCategoryThumb}",
            ImageLoader.getImageListener(binding.ivCategoryImg,
                R.drawable.ic_default_image,
                R.drawable.ic_error
            )
        )
        binding.ivCategoryImg.setImageUrl(
            "https://rjtmobile.com/grocery/images/${category.strCategoryThumb}",
            imageLoader)
    }
}