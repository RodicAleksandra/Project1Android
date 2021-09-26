package com.example.project.holder

import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.example.project.R
import com.example.project.data.Product
import com.example.project.databinding.HolderProductBinding


class ProductHolder(val binding: HolderProductBinding): RecyclerView.ViewHolder(binding.root) {

    val tvCategory = binding.tvProductName
    val tvDesc = binding.tvProductDesc
    val tvPrice = binding.tvPrice
    val ivImage = binding.ivProductImage

    fun bind(product: Product, imageLoader: ImageLoader){
        tvCategory.text = product.productName
        tvPrice.text = "$${product.price}"
        tvDesc.text = product.description
        imageLoader.get(
            "https://rjtmobile.com/grocery/images/${product.image}",
            ImageLoader.getImageListener(binding.ivProductImage,
                R.drawable.ic_default_image, R.drawable.ic_error)
        )
        ivImage.setImageUrl(
            "https://rjtmobile.com/grocery/images/${product.image}",
            imageLoader)
    }
}