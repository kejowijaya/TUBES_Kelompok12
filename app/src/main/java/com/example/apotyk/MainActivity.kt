package com.example.apotyk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.apotyk.user.User
import com.example.apotyk.user.UserDB
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername:TextInputLayout
    private lateinit var inputPassword:TextInputLayout
    private lateinit var mainLayout:ConstraintLayout
    lateinit var  mBundle: Bundle
    val db by lazy { UserDB(this) }
    lateinit var Users: List<User>
    var checkLogin=false
    lateinit var username:String
    lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            Users = db.userDao().getUsers()
            finish()
        }

        setTitle("User Login")
        mBundle = Bundle()
        inputUsername=findViewById(R.id.inputLayoutUsername)
        inputPassword=findViewById(R.id.inputLayoutPassword)
        mainLayout=findViewById(R.id.mainLayout)
        val btnRegister:Button = findViewById(R.id.btnMainRegister)
        val btnLogin:Button=findViewById(R.id.btnLogin)

        btnRegister.setOnClickListener{
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }


        btnLogin.setOnClickListener{
            username=inputUsername.getEditText()?.getText().toString()
            password=inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin=false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin=false
            }

            for(user in Users){
                if(username==user.username&&password==user.password)checkLogin=true
                if(checkLogin){
                    val moveHome=Intent(this@MainActivity,HomeActivity::class.java)
                    mBundle.putString("username",user.username)
                    mBundle.putString("password",user.password)
                    mBundle.putString("tanggalLahir",user.tanggalLahir)
                    mBundle.putString("email",user.email)
                    mBundle.putString("nomorTelepon",user.nomorTelepon)
                    mBundle.putInt("id",user.id)
                    moveHome.putExtra("user", mBundle)
                    startActivity(moveHome)
                    break
                }
            }
            if(!checkLogin){
                inputUsername.setError("Username atau Password salah")
                inputPassword.setError("Username atau Password salah")
            }

        }
        println("test")
    }
}