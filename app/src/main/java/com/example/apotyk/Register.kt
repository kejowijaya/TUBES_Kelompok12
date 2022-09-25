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

class Register : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var email : TextInputEditText
    private lateinit var tanggalLahir : TextInputEditText
    private lateinit var nomorTelepon : TextInputEditText
    private lateinit var btnRegister : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("Register")

        username = findViewById(R.id.etUsername)
        password = findViewById(R.id.etPassword)
        email = findViewById(R.id.etEmail)
        nomorTelepon = findViewById(R.id.etNomorTelepon)
        tanggalLahir = findViewById(R.id.etTanggalLahir)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().addUser(
                    User((Math.random() * (10000 - 100 + 1)).toInt(), username.text.toString(), tanggalLahir.text.toString(), email.text.toString(), nomorTelepon.text.toString(),
                        password.text.toString())
                )
                finish()
            }

            startActivity(intent)
        }

    }
}