package com.example.project.holder


import androidx.recyclerview.widget.RecyclerView
import com.example.project.data.CartItem
import com.example.project.databinding.HolderCartitemBinding
import com.squareup.picasso.Picasso



class CartItemHolder(val binding: HolderCartitemBinding): RecyclerView.ViewHolder(binding.root) {

    val tvName = binding.tvProductName
    val tvPrice = binding.tvPrice
    val tvQuantity = binding.tvQuantity
    val ivImage = binding.ivProductImage
    val picasso = Picasso.get()

    fun bind(cartItem: CartItem) {
        tvName = cartItem.cartProductName
        tvPrice.text = "$${
            cartItem.cartPrice * cartItem.cartQuantity"
            tvQuantity.text = cartItem.quantity.toString()
            picasso.load("https://rjtmobile.com/grocery/images/${cartItem.image}").into(ivImage)
        }
    }
}