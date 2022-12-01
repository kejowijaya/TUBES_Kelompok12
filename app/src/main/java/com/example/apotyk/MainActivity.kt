package com.example.apotyk

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.api.UserApi
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.model.Obat
import com.example.apotyk.model.User
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.master.permissionhelper.PermissionHelper
import kotlinx.coroutines.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var queue: RequestQueue? = null
    private val loginPreference = "login"
    var sharedPreferencesLogin: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        queue = Volley.newRequestQueue(this)

        setTitle("User Login")
        val inputUsername = binding.inputLayoutUsername
        val inputPassword= binding.inputLayoutPassword

        val permissionHelper = PermissionHelper(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            , 100)
        permissionHelper?.denied {
            Log.d("TAG", "Permission denied")
        }
        //Request all permission
        permissionHelper?.requestAll {
            Log.d("TAG", "All permission granted")
        }
        //Request individual permission
        permissionHelper?.requestIndividual {
            Log.d("TAG", "Individual Permission Granted")
        }

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
        sharedPreferencesLogin = getSharedPreferences(loginPreference, Context.MODE_PRIVATE)
        val editorLogin: SharedPreferences.Editor = sharedPreferencesLogin!!.edit()

        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, UserApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val json = JSONObject(response)
                var user : Array<User> = gson.fromJson(
                    json.getJSONArray("data").toString(),
                    Array<User>::class.java)
                var isLogin = false
                for (i in user.indices) {
                    if (user[i].username == username && user[i].password == password) {
                        editorLogin.putLong("idUser", user[i].id!!)
                        editorLogin.commit()
                        isLogin = true
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("id", user[i].id)
                        startActivity(intent)
                        break
                    }
                }
                if (!isLogin) {
                    Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        queue?.add(stringRequest)
    }

}