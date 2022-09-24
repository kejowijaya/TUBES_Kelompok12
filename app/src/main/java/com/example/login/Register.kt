package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.login.user.User
import com.example.login.user.UserDB
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
            val mBundle = Bundle()
            mBundle.putString("username",username.text.toString())
            mBundle.putString("password",password.text.toString())
            mBundle.putString("tanggalLahir",tanggalLahir.text.toString())
            mBundle.putString("email",email.text.toString())
            mBundle.putString("nomorTelepon",nomorTelepon.text.toString())
            intent.putExtra("register", mBundle)

            if(username.text.toString().length == 0){
                CoroutineScope(Dispatchers.IO).launch {
                    db.userDao().addUser(
                        User(1, "a",
                            "a")
                    )
                    finish()
                }

                startActivity(intent)
            }

            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().addUser(
                    User((Math.random() * (10000 - 100 + 1)).toInt(), username.text.toString(),
                        password.text.toString())
                )
                finish()
            }

            startActivity(intent)
        }

    }
}