package com.example.apotyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.user.User
import com.example.apotyk.user.UserDB
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var  mBundle: Bundle
    val db by lazy { UserDB(this) }
    lateinit var users: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("User Login")

        val inputUsername = binding.inputLayoutUsername
        val inputPassword= binding.inputLayoutPassword


        binding.btnMainRegister.setOnClickListener{
            val intent = Intent(this,Register::class.java)

            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener(View.OnClickListener {
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