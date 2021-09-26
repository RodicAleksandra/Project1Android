package com.example.project

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project.adapter.CategoryAdapter
import com.example.project.data.Category
import com.example.project.databinding.ActivityDashboardBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    lateinit var requestQueue: RequestQueue
    var selectedCategory: Category? = null
    lateinit var imageLoader: ImageLoader
    lateinit var adapter: CategoryAdapter
    var categories: ArrayList<Category> = ArrayList<Category>()
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var currentActivity: AppCompatActivity



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedCategory = intent.extras?.getParcelable<Category>("category")

        requestQueue = Volley.newRequestQueue(baseContext)
        imageLoader = ImageLoader(requestQueue, cache)
        binding.rvCategories.layoutManager = LinearLayoutManager(baseContext)

loadCategories()
    }

    private fun loadCategories() {
        val request = JsonObjectRequest(
            Request.Method.GET,
            "https://grocery-second-app.herokuapp.com/api/category",
            null,
                    Response.Listener<JSONObject> { response ->

                val categoriesJson = response.getJSONArray("categories").toString()
                val gson = Gson()
                val type = object : TypeToken<ArrayList<Category>>() {}.type

                categories = gson.fromJson(categoriesJson, type)
                adapter = CategoryAdapter(categories, imageLoader)
                adapter.setOnCategoryClickListener { category, position ->
                    val catIntent = Intent(baseContext, DashboardActivity::class.java)
                    catIntent.putExtra("category", category)
                    startActivity(catIntent)
                }

                binding.rvCategories.adapter = adapter


            },
            { error ->
                error.printStackTrace()
                Toast.makeText(baseContext, "Error is : ${error.toString()}", Toast.LENGTH_LONG)
                    .show()
            }
        )

        requestQueue.add(request)
    }



    val cache = object : ImageLoader.ImageCache {

        val lruCache: LruCache<String, Bitmap> = LruCache(100)

        override fun getBitmap(url: String?): Bitmap? {
            return lruCache[url]
        }

        override fun putBitmap(url: String?, bitmap: Bitmap?) {

            url?.let {
                lruCache.put(it, bitmap)
            }
        }

    }
}


