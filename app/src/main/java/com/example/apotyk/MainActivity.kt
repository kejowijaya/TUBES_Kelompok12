package com.example.apotyk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.user.User
import com.example.apotyk.user.UserDB
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername:TextInputLayout
    private lateinit var inputPassword:TextInputLayout
    private lateinit var mainLayout:ConstraintLayout
    lateinit var  mBundle: Bundle
    lateinit var vUsername : String
    lateinit var vPassword : String
    val db by lazy { UserDB(this) }
    lateinit var users: List<User>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setTitle("User Login")

        inputUsername=findViewById(R.id.inputLayoutUsername)
        inputPassword=findViewById(R.id.inputLayoutPassword)
        mainLayout=findViewById(R.id.mainLayout)
        val btnRegister:Button = findViewById(R.id.btnMainRegister)
        val btnLogin:Button=findViewById(R.id.btnLogin)

        btnRegister.setOnClickListener{
            val intent = Intent(this,Register::class.java)

            startActivity(intent)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            val username:String=inputUsername.getEditText()?.getText().toString()
            val password:String=inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
            }else if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
            }else {
                checkLogin(username, password)
            }
        })
    }
    fun checkLogin(username:String,password:String){
        CoroutineScope(Dispatchers.IO).launch {
            users = db.userDao().getUsers()
            users.forEach{
                if(it.username==username&&it.password==password){
                    val moveHome=Intent(this@MainActivity,HomeActivity::class.java)
                    mBundle = Bundle()
                    mBundle.putInt("id",it.id)
                    mBundle.putString("username",it.username)
                    mBundle.putString("password",it.password)
                    mBundle.putString("tanggalLahir",it.tanggalLahir)
                    mBundle.putString("email",it.email)
                    mBundle.putString("nomorTelepon",it.nomorTelepon)
                    moveHome.putExtra("login", mBundle)
                    startActivity(moveHome)
                }
            }
        }
    }




}