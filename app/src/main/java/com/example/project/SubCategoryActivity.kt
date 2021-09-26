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
import com.example.project.adapter.SubCategoryAdapter
import com.example.project.data.SubCategory
import com.example.project.databinding.ActivitySubcategoryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class SubCategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubcategoryBinding
    lateinit var requestQueue: RequestQueue
    lateinit var imageLoader: ImageLoader
    lateinit var adapter: SubCategoryAdapter
    var subcategories: ArrayList<SubCategory> = ArrayList<SubCategory>()

    companion object{
        var catId = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        catId = intent.extras?.getInt("catId")?:-1

        requestQueue = Volley.newRequestQueue((baseContext))
        imageLoader = ImageLoader(requestQueue, cache)
        binding.rvSubCategories.layoutManager = LinearLayoutManager(baseContext)

        loadSubCategories()
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

    private fun loadSubCategories() {
        val request = object: JsonObjectRequest(
            "https://grocery-second-app.herokuapp.com/api/subcategory/$catId",
            Response.Listener<JSONObject> { response ->

                val subcategoriesJSON = response.getJSONArray("data").toString()
                val gson = Gson()
                val type = object: TypeToken<ArrayList<SubCategory>>() {}.type

                subcategories = gson.fromJson(subcategoriesJSON, type)
                adapter = SubCategoryAdapter(subcategories, imageLoader)

                adapter.setOnSubcategoryClickListener(){ subcategory, position ->
                    val ProductsIntent = Intent(baseContext, ProductsActivity::class.java)
                    ProductsIntent.putExtra("subId", subcategory.subCatId)
                    startActivity(ProductsIntent)
                }
                binding.rvSubCategories.adapter = adapter
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(baseContext, "Error: ${error.toString()}", Toast.LENGTH_LONG).show()
            }
        ){}

        requestQueue.add(request)
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