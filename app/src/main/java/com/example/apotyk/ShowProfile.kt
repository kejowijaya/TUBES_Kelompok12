package com.example.apotyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.apotyk.databinding.ActivityShowProfileBinding

class ShowProfile : AppCompatActivity() {
    lateinit var mBundle: Bundle
    private lateinit var binding: ActivityShowProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mBundle = intent.getBundleExtra("login")!!
        setData()
        binding.btnEditUser.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("user", mBundle)
            startActivity(intent)
        }
    }

    fun setData() {
        val username = binding.etSessionUsername
        val email = binding.etSessionEmail
        val tanggalLahir = binding.etSessionTanggalLahir
        val nomorTelepon = binding.etSessionNomorTelepon
        username.text = mBundle.getString("username")
        email.text = mBundle.getString("email")
        tanggalLahir.text = mBundle.getString("tanggalLahir")
        nomorTelepon.text = mBundle.getString("nomorTelepon")
    }
}