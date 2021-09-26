package com.example.project

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.LruCache
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project.adapter.ProductAdapter
import com.example.project.data.Product
import com.example.project.databinding.ActivityProductsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class ProductsActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductsBinding
    lateinit var requestQueue: RequestQueue
    lateinit var imageLoader: ImageLoader
    lateinit var adapter: ProductAdapter
    var products: ArrayList<Product> = ArrayList<Product>()

    companion object{
        var subId = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subId = intent.extras?.getInt("subId")?:-1

        requestQueue = Volley.newRequestQueue(baseContext)
        imageLoader = ImageLoader(requestQueue, cache)
        binding.rvProducts.layoutManager = LinearLayoutManager(baseContext)

        loadProducts()
    }

    val cache = object: ImageLoader.ImageCache {
        val lruCache: LruCache<String, Bitmap> = LruCache(100)

        override fun getBitmap(url: String?): Bitmap? {
            return lruCache[url]
        }

        override fun putBitmap(url: String?, bitmap: Bitmap?) {
            url?.let{
                lruCache.put(it, bitmap)
            }
        }
    }

    private fun loadProducts() {
        val request = object: JsonObjectRequest(
            "https://grocery-second-app.herokuapp.com/api/products/sub/${subId}",
            Response.Listener<JSONObject> { response ->

                val productsJSON = response.getJSONArray("data").toString()
                val gson = Gson()
                val type = object: TypeToken<ArrayList<Product>>() {}.type

                products = gson.fromJson(productsJSON, type)
                adapter = ProductAdapter(products, imageLoader)

                adapter.setOnProductClickListener(){ product, position ->
                    val ProductDetailIntent = Intent(baseContext, ProductDetailActivity::class.java)
                    ProductDetailIntent.putExtra("product", product)
                    startActivity(ProductDetailIntent)
                }
                binding.rvProducts.adapter = adapter
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(baseContext, "Error: ${error.toString()}", Toast.LENGTH_LONG).show()
            }
        ){}
        requestQueue.add(request)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }


}