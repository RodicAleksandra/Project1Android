package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project.adapter.OrderAdapter
import com.example.project.data.Order
import com.example.project.databinding.ActivityViewordersBinding

class ViewOrdersActivity : AppCompatActivity() {

    lateinit var binding: ActivityViewordersBinding
    lateinit var requestQueue: RequestQueue
    lateinit var adapter: OrderAdapter
    var orders: ArrayList<Order> = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewordersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(baseContext)

        binding.rvOrders.layoutManager = LinearLayoutManager(baseContext)

        loadOrders()
    }

    private fun loadOrders() {
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        val userId = sharedPref.getString("userId", "null")

        val request = object: JsonObjectRequest(
            "https://grocery-second-app.herokuapp.com/api/orders/$userId",
            Response.Listener { response ->
                val ordersJSON = response.getJSONArray("data")
                for(i in 0 until ordersJSON.length()){
                    val orderJSON = ordersJSON.getJSONObject(i)
                    val _id = orderJSON.getString("_id")
                    val date = orderJSON.getString("date")
                    var items = 0
                    var total = 0.0
                    if(orderJSON.has("items")){
                        items = orderJSON.getInt("items")
                    }
                    if(orderJSON.has("total")){
                        total = orderJSON.getDouble("total")
                    }

                    val order = Order(_id, "Processing", total, 0.0, items, date)

                    orders.add(order)
                    Log.d("ORDERS", "$orders")
                }

                adapter = OrderAdapter(orders)
                binding.rvOrders.adapter = adapter
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