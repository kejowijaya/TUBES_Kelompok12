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
    lateinit var btnUpdate: Button
    lateinit var username: TextInputEditText
    lateinit var email: TextInputEditText
    lateinit var tanggalLahir: TextInputEditText
    lateinit var nomorTelepon: TextInputEditText
    lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        btnUpdate =  findViewById(R.id.btnUpdateUser)
        username = findViewById(R.id.etEditUsername)
        email = findViewById(R.id.etEditEmail)
        tanggalLahir = findViewById(R.id.etEditTanggalLahir)
        nomorTelepon = findViewById(R.id.etEditNomorTelepon)
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
        username.setText(mBundle.getString("username"))
        email.setText(mBundle.getString("email"))
        tanggalLahir.setText(mBundle.getString("tanggalLahir"))
        nomorTelepon.setText(mBundle.getString("nomorTelepon"))
        password = mBundle.getString("password").toString()
    }
}