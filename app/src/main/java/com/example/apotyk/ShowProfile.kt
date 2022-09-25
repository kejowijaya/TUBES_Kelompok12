package com.example.apotyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ShowProfile : AppCompatActivity() {
    lateinit var mBundle: Bundle
    lateinit var editUser: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
        mBundle = intent.getBundleExtra("login")!!
        setData()
        editUser = findViewById(R.id.btnEditUser)
        editUser.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("user", mBundle)
            startActivity(intent)
        }
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