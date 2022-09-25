package com.example.apotyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername:TextInputLayout
    private lateinit var inputPassword:TextInputLayout
    private lateinit var mainLayout:ConstraintLayout
    lateinit var  mBundle: Bundle

    lateinit var vUsername : String
    lateinit var vPassword : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getBundle()

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
            var checkLogin=false
            val username:String=inputUsername.getEditText()?.getText().toString()
            val password:String=inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin=false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin=false
            }

            if(username==vUsername&&password==vPassword)checkLogin=true
            if(!checkLogin)return@OnClickListener
            val moveHome=Intent(this@MainActivity,HomeActivity::class.java)
            moveHome.putExtra("login", mBundle)
            startActivity(moveHome)
        })
    }

    fun getBundle() {
        mBundle = intent.getBundleExtra("register")!!
        vUsername = mBundle.getString("username")!!
        vPassword = mBundle.getString("password")!!
    }
}