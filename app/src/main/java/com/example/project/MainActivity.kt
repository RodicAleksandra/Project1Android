package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("user_info", MODE_PRIVATE)
        if (pref.contains("user_details")){ //means user already logged in before

            handler.sendEmptyMessageDelayed(LAUNCH_DASHBOARD,2000)
        }else{
            handler.sendEmptyMessageDelayed(LAUNCH_LOGIN_SCREEN,2000)
        }

        handler.sendEmptyMessageDelayed(LAUNCH_LOGIN_SCREEN, 2000)
    }
    companion object {

        const val LAUNCH_LOGIN_SCREEN = 100

        const val LAUNCH_DASHBOARD = 200
    }

    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == LAUNCH_LOGIN_SCREEN) {
                startActivity(Intent(baseContext,LoginActivity::class.java))
                finish()

            }else if (msg.what == LAUNCH_DASHBOARD)
            {
                startActivity(Intent(baseContext, DashboardActivity::class.java))
                finish()

            }
        }
    }


}