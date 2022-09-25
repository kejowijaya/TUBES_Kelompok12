package com.example.apotyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ShowProfile : AppCompatActivity() {
    lateinit var mBundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
        mBundle = intent.getBundleExtra("login")!!
        setData()
    }

    fun setData() {
        val username: TextView = findViewById(R.id.etSessionUsername)
        val email: TextView = findViewById(R.id.etSessionEmail)
        val tanggalLahir: TextView = findViewById(R.id.etSessionTanggalLahir)
        val nomorTelepon: TextView = findViewById(R.id.etSessionNomorTelepon)
        username.text = mBundle.getString("username")
        email.text = mBundle.getString("email")
        tanggalLahir.text = mBundle.getString("tanggalLahir")
        nomorTelepon.text = mBundle.getString("nomorTelepon")
    }
}