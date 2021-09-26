package com.example.project

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(baseContext)




        loadSharedPreferences()

        binding.btnSignIn.setOnClickListener {

            signIn()

        }

        binding.btnRemember.setOnClickListener {

            startActivity(Intent(baseContext, RegisterActivity::class.java))

        }}

    private fun loadSharedPreferences() {
        val sharedPref = getSharedPreferences("app_settings ", MODE_PRIVATE)

        binding.etEmailLogIn.hint = sharedPref.getString("email", "")
        binding.etPassword.hint = sharedPref.getString("password", "")
    }


    private fun signIn(){

        val email = binding.etEmailLogIn.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.etEmailLogIn.error = "Email is required to Log In"
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Please enter your password to Log In"
        }

        val loginRequestData = JSONObject()
        loginRequestData.put("email", email)
        loginRequestData.put("password", password)

        val pd = ProgressDialog(this)
        pd.setTitle("Please Wait")
        pd.setMessage("working on it")
        pd.setCancelable(false)


        val request = object : JsonObjectRequest(
            Request.Method.POST,
            "https://grocery-second-app.herokuapp.com/api/auth/login",
            loginRequestData,
            {
                it.getJSONObject("user").getString("firstName")
                pd.dismiss()
                if (it.has("token")) {
                    //storing response of login to shared pref.
                    //if user_info exists, user logged in earler
                    val pref = getSharedPreferences("user_info", MODE_PRIVATE)
                    pref.edit().putString("user_details", it.toString()).apply()

                    Toast.makeText(baseContext, "Login Successful", Toast.LENGTH_LONG).show()
                    startActivity(Intent(baseContext, DashboardActivity::class.java))
                    finish()
                } else if (it.has("error") && it.getBoolean("error")) {

                    var message = "failed to login"
                    if (it.has("message")) {
                        message = it.getString("message")
                    }
                    Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()

                } else {

                    Toast.makeText(
                        baseContext,
                        "Unknown error, please retry your login",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            { error ->
                Toast.makeText(baseContext, "Error: $error ", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            }
        ) {}

        request.retryPolicy = DefaultRetryPolicy(10*1000,1, 1F)
        requestQueue.add(request)


    } //end of sign in

}