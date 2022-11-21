package com.example.apotyk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.api.UserApi
import com.example.apotyk.databinding.ActivityEditProfileBinding
import com.example.apotyk.model.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_edit_obat.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var queue: RequestQueue? = null
    private val loginPreference = "login"
    private var idUser:Long = 0
    var sharedPreferencesLogin: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesLogin = this?.getSharedPreferences(loginPreference, Context.MODE_PRIVATE)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnUpdate = binding.btnUpdateUser
        val username = binding.etEditUsername
        val password = binding.etEditPassword
        val email = binding.etEditEmail
        val tanggalLahir = binding.etEditTanggalLahir
        val nomorTelepon = binding.etEditNomorTelepon

        queue = Volley.newRequestQueue(this)
        idUser = sharedPreferencesLogin!!.getLong("idUser",0)

        getUserById(idUser)
        btnUpdate.setOnClickListener {
            val user = User(
                    username = username.text.toString(),
                    password = password.text.toString(),
                    email = email.text.toString(),
                    tanggal_lahir = tanggalLahir.text.toString(),
                    nomor_telepon = nomorTelepon.text.toString()
                )

                    val stringRequest: StringRequest = object :
                        StringRequest(Method.PUT, UserApi.UPDATE_URL + idUser, Response.Listener { response ->
                            val gson = Gson()

                            var user = gson.fromJson(response, User::class.java)

                            if(user != null)
                                Toast.makeText(this@EditProfile, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                }, Response.ErrorListener { error ->
                    try {
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditProfile,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditProfile, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = user.username
                    params["password"] = user.password
                    params["email"] = user.email
                    params["tanggal_lahir"] = user.tanggal_lahir
                    params["nomor_telepon"] = user.nomor_telepon
                    return params
                }
            }
            queue!!.add(stringRequest)

            val intent = Intent(this, ShowProfile::class.java)
            startActivity(intent)
        }

    }

    private fun getUserById(id: Long) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val json = JSONObject(response)
                var user = gson.fromJson(
                    json.getJSONArray("data")[0].toString(),
                    User::class.java
                )
                binding!!.etEditUsername.setText(user.username)
                binding!!.etEditPassword.setText(user.password)
                binding!!.etEditEmail.setText(user.email)
                binding!!.etEditTanggalLahir.setText(user.tanggal_lahir)
                binding!!.etEditNomorTelepon.setText(user.nomor_telepon)


                Toast.makeText(this@EditProfile,"Data berhasil diambil", Toast.LENGTH_SHORT).show()

            },
                Response.ErrorListener{ error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@EditProfile, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
}