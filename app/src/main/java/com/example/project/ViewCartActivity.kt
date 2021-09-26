package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project.adapter.CartItemAdapter
import com.example.project.data.CartItem
import com.example.project.databinding.ActivityViewcartBinding

import com.example.project.databinding.ActivityViewordersBinding
import com.example.project.sql.CartDao
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Array.get
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ViewCartActivity : AppCompatActivity() {

    lateinit var binding: ActivityViewcartBinding
    lateinit var requestQueue: RequestQueue
    lateinit var adapter: CartItemAdapter
    lateinit var cartDao: CartDao
    lateinit var cartItems: ArrayList<CartItem>
    lateinit var picasso: Picasso


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewcartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        picasso = Picasso.get()
        cartDao = CartDao(baseContext)
        cartItems = cartDao.getItems()

        binding.rvCart.layoutManager = LinearLayoutManager(baseContext)

        populateList()

        binding.btnCheckout.setOnClickListener{
            checkout()
        }

        requestQueue = Volley.newRequestQueue(baseContext)
    }

    private fun checkout() {
        val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
        val userId = sharedPref.getString("userId", "null")


        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formattedDate = formatter.format(date)

        val orderRequestData = JSONObject()

        orderRequestData.put("userId", userId)
        orderRequestData.put("date", date)

        var totalPrice: Double = 0.0
        var totalItems: Int = 0
        val cartRequestData = JSONArray()

        for(i in 0 until cartItems.size){
            val cartItemRequestData = JSONObject()
            cartItemRequestData.put("_id", cartItems[i].cartProductId)
            cartItemRequestData.put("quantity", cartItems[i].cartQuantity)
            cartItemRequestData.put("price", cartItems[i].cartPrice)
            cartItemRequestData.put("productName", cartItems[i].cartProductName)
            totalPrice += (cartItems[i].cartQuantity * cartItems[i].cartPrice)
            totalItems += cartItems[i].cartQuantity

            cartRequestData.put(i, cartItemRequestData)
        }
        orderRequestData.put("total", totalPrice)
        orderRequestData.put("products", cartRequestData)
        orderRequestData.put("items", totalItems)


        val request = JsonObjectRequest(
            Request.Method.POST,
            "https://grocery-second-app.herokuapp.com/api/orders",
            orderRequestData,
            Response.Listener { response ->
                if(response.has("error") && response.getBoolean("error")){
                    Toast.makeText(baseContext, "Order Placement Failed: ${response.getString("message")}", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(baseContext, "Order Placed Successfully", Toast.LENGTH_LONG).show()
                    cartItems.clear()
                    adapter.notifyDataSetChanged()
                    cartDao.clearCart()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(baseContext, "Error is: $error", Toast.LENGTH_LONG).show()
            }
        )
        requestQueue.add(request)
    }

    private fun populateList() {
        adapter = CartItemAdapter(cartItems, cartDao)

        binding.rvCart.adapter = adapter
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