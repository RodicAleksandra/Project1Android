package com.example.project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.data.CartItem
import com.example.project.databinding.HolderCartitemBinding
import com.example.project.holder.CartItemHolder

class CartItemAdapter(val cartItemList: ArrayList<CartItem>, val cartDao: CartDao): RecyclerView.Adapter<CartItemHolder>() {

    lateinit var addClickListener: (CartItem, Int) -> Unit
    lateinit var binding: HolderCartitemBinding

    fun setOnAddClickListener(listener: (CartItem, Int) -> Unit){
        addClickListener = listener
    }

    lateinit var subtractClickListener: (CartItem, Int) -> Unit

    fun setOnSubtractClickListener(listener: (CartItem, Int) -> Unit){
        subtractClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = HolderCartitemBinding.inflate(layoutInflater, parent, false)
        return CartItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemHolder, position: Int) {
        cartItemList?.let {
            val cartItem = it[position]
            holder.bind(it[position])
            binding.btnAdd.setOnClickListener{
                //Toast.makeText(, "btnPlus: $cartItem\nPosition: $position", Toast.LENGTH_LONG).show()
                when (cartItem.quantity) {
                    0 -> {
                        cartItem.quantity++
                        holder.binding.btnSubtract.text = "-"
                        cartDao.updateItem(cartItem)
                        holder.binding.tvQuantity.text = cartItem.quantity.toString()
                    }
                    else -> {
                        cartItem.quantity++
                        cartDao.updateItem(cartItem)
                        holder.binding.tvQuantity.text = cartItem.quantity.toString()
                    }
                }
                notifyDataSetChanged()
            }
            binding.btnSubtract.setOnClickListener{
                when(cartItem.quantity){
                    0 -> {
                        cartDao.deleteItem(cartItem.itemId)
                        holder.binding.btnSubtract.text = "-"
                        cartItemList.remove(cartItem)
                    }
                    1 -> { cartItem.quantity--
                        cartDao.updateItem(cartItem)
                        holder.binding.btnSubtract.text = "Remove"
                        holder.binding.tvQuantity.text = cartItem.quantity.toString()
                    }
                    else -> {cartItem.quantity--
                        cartDao.updateItem(cartItem)
                        holder.binding.tvQuantity.text = cartItem.quantity.toString()
                    }
                }
                notifyDataSetChanged()
            }

        }
    }

    override fun getItemCount() = cartItemList.size

}