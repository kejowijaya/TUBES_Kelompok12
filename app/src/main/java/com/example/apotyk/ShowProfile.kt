package com.example.apotyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.apotyk.databinding.ActivityShowProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        binding.bottomNav.selectedItemId = R.id.menu_user
        binding.bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_obat-> {
                    val moveHome = Intent(this,HomeActivity::class.java)
                    moveHome.putExtra("login", mBundle)
                    startActivity(moveHome)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_riwayat -> {
                    val moveRiwayat = Intent(this,ShowObat::class.java)
                    moveRiwayat.putExtra("login", mBundle)
                    startActivity(moveRiwayat)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_user -> {

                }
                R.id.menu_exit -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
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