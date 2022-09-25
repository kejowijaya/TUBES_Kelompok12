package com.example.apotyk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    private val myPreference = "myPref"
    private val name = "nameKey"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(myPreference,
            Context.MODE_PRIVATE)
        if(sharedPreferences!!.contains(name)){
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }else{
            setContentView(R.layout.splash_screen)
            Handler().postDelayed({
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
                finish()
            }, 3000)

            val editor: SharedPreferences.Editor =
                sharedPreferences!!.edit()
            editor.putString(name, "oke")
            editor.apply()
        }
    }
}

