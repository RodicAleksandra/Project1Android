package com.example.project.holder

import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.example.project.R
import com.example.project.data.SubCategory
import com.example.project.databinding.HolderSubcategoryBinding

class SubCategoryHolder(val binding: HolderSubcategoryBinding): RecyclerView.ViewHolder(binding.root) {

    val tvCategory = binding.tvSubCategory
    val tvDesc = binding.tvSubCategoryDesc
    val ivImage = binding.ivSubCategoryImg

    fun bind(subcategory: SubCategory, imageLoader: ImageLoader){
        tvCategory.text = subcategory.subCatName
        tvDesc.text = subcategory.subCatDescription
        imageLoader.get(
            "https://rjtmobile.com/grocery/images/${subcategory.subCatImage}",
            ImageLoader.getImageListener(binding.ivSubCategoryImg,
                R.drawable.ic_default_image, R.drawable.ic_error)
        )
        binding.ivSubCategoryImg.setImageUrl(
            "https://rjtmobile.com/grocery/images/${subcategory.subCatImage}",
            imageLoader)
    }
}