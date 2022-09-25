package com.example.apotyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.apotyk.user.User
import com.example.apotyk.user.UserDB
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfile : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    lateinit var mBundle: Bundle
    var btnUpdate: Button =  findViewById(R.id.btnUpdateUser)
    var username: TextInputEditText = findViewById(R.id.etEditUsername)
    var email: TextInputEditText = findViewById(R.id.etEditEmail)
    var tanggalLahir: TextInputEditText = findViewById(R.id.etEditTanggalLahir)
    var nomorTelepon: TextInputEditText = findViewById(R.id.etEditNomorTelepon)
    lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)
        mBundle = intent.getBundleExtra("user")!!
        setData()
        btnUpdate.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)

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
        username.setText(mBundle.getString("username"))
        email.setText(mBundle.getString("email"))
        tanggalLahir.setText(mBundle.getString("tanggalLahir"))
        nomorTelepon.setText(mBundle.getString("nomorTelepon"))
        password = mBundle.getString("password").toString()
    }
}