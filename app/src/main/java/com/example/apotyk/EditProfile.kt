package com.example.apotyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.apotyk.databinding.ActivityEditProfileBinding
import com.example.apotyk.databinding.ActivityRegisterBinding
import com.example.apotyk.user.User
import com.example.apotyk.user.UserDB
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfile : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    lateinit var mBundle: Bundle
    private lateinit var binding: ActivityEditProfileBinding

    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnUpdate =  binding.btnUpdateUser
        val username = binding.etEditUsername
        val email = binding.etEditEmail
        val tanggalLahir = binding.etEditTanggalLahir
        val nomorTelepon = binding.etEditNomorTelepon

        mBundle = intent.getBundleExtra("user")!!
        setData()
        btnUpdate.setOnClickListener{
            val intent = Intent(this, ShowProfile::class.java)
            mBundle.putString("username",username.text.toString())
            mBundle.putString("tanggalLahir",tanggalLahir.text.toString())
            mBundle.putString("email",email.text.toString())
            mBundle.putString("nomorTelepon",nomorTelepon.text.toString())
            intent.putExtra("login", mBundle)
            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().updateUser(
                    User((Math.random() * (10000 - 100 + 1)).toInt(), username.text.toString(), tanggalLahir.text.toString(), email.text.toString(), nomorTelepon.text.toString(),
                        password)
                )
                finish()
            }

            startActivity(intent)
        }
    }

    fun setData() {
        val username = binding.etEditUsername
        val email = binding.etEditEmail
        val tanggalLahir = binding.etEditTanggalLahir
        val nomorTelepon = binding.etEditNomorTelepon
        username.setText(mBundle.getString("username"))
        email.setText(mBundle.getString("email"))
        tanggalLahir.setText(mBundle.getString("tanggalLahir"))
        nomorTelepon.setText(mBundle.getString("nomorTelepon"))
        password = mBundle.getString("password").toString()
    }
}