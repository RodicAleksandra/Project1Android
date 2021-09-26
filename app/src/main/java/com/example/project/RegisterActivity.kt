package com.example.project

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.project.databinding.ActivityRegisterBinding
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(baseContext)


        binding.btnSignUp.setOnClickListener {

            signUp()
        }

        binding.btnAlreadyHave.setOnClickListener {

            startActivity(Intent(baseContext, LoginActivity::class.java))

        }
    }

    private fun signUp() {
        var hasError = false
        val firstName = binding.etName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val address = binding.etAddress.text.toString()
        val mobile = binding.etMobile.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (firstName.isEmpty()) {

            binding.etName.error = "Enter the name"
            hasError = true

        }
        if (lastName.isEmpty()) {
            binding.etLastName.error = "Enter the last name"
            hasError = true

        }
        if (address.isEmpty()) {
            binding.etAddress.error = "Enter the address"
            hasError = true

        }
        if (mobile.isEmpty()) {
            binding.etMobile.error = "Enter the number"
            hasError = true

        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Enter the email"
            hasError = true

        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Enter the password"
            hasError = true
        }

        val sharedPref = getSharedPreferences("loginRequestData", MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("firstName", firstName)
        editor.putString("lastName", lastName)
        editor.putString("address", address)
        editor.putString("mobile", mobile)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()


        val userCreateAccount = JSONObject()
        userCreateAccount.put("firstName", firstName)
        userCreateAccount.put("mobile", mobile)
        userCreateAccount.put("password", password)
        userCreateAccount.put("email", email)


        val request = object : JsonObjectRequest(
            Request.Method.POST,
            "https://grocery-second-app.herokuapp.com/api/auth/register",
            userCreateAccount,
            Response.Listener<JSONObject> { response ->
                if (response.getBoolean("error")) {
                    Toast.makeText(
                        baseContext, "invalid info",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(baseContext, DashboardActivity::class.java))
                } else {

                    val sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString("firstName", response.getString("firstName"))
                    startActivity(Intent(baseContext, DashboardActivity::class.java))

                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(baseContext, "Error: $error ", Toast.LENGTH_LONG).show()
            }
        ){}
        requestQueue.add(request)
        request.retryPolicy = DefaultRetryPolicy(10 * 1000, 1, 1F)
    }

    }
