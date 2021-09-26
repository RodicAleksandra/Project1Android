package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.data.CartItem
import com.example.project.data.Product
import com.example.project.databinding.ActivityProductDetailBinding
import com.example.project.sql.CartDao
import com.squareup.picasso.Picasso
import java.lang.reflect.Array.get

class ProductDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductDetailBinding
    lateinit var product: Product
    lateinit var picasso: Picasso

    lateinit var cartDao: CartDao

    companion object{
        var productId:String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartDao = CartDao(baseContext)

        product = intent.extras?.get("product") as Product

        binding.tvProductName.text = product.productName
        binding.tvPrice.text = "$${product.price}"
        binding.tvProductDesc.text = product.description

        picasso = Picasso.get()

        picasso.load("https://rjtmobile.com/grocery/images/${product.image}").into(binding.ivProductImage)

        setupEvents()
    }

    private fun setupEvents() {
        binding.btnAddToCart.setOnClickListener{
            try {
                if(cartDao.hasItem(product)){
                    Toast.makeText(baseContext,"Item Found In Cart", Toast.LENGTH_LONG).show()
                }
                else{
                    val item = CartItem(0, product.productId, product.productName, product.image,
                        1, product.price)
                    cartDao.addItem(item)
                    Toast.makeText(baseContext, "Item Added to Cart", Toast.LENGTH_LONG).show()
                }
            }catch(e: Exception){
                Toast.makeText(baseContext, "An Error has occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnViewCart.setOnClickListener{
            startActivity(Intent(baseContext, ViewCartActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_cart -> {
                val cartIntent = Intent(baseContext, ViewCartActivity::class.java)
                startActivity(cartIntent)
            }
            R.id.action_orders -> {
                val ordersIntent = Intent(baseContext, ViewOrdersActivity::class.java)
                startActivity(ordersIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}