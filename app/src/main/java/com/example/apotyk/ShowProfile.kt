package com.example.apotyk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.api.UserApi
import com.example.apotyk.camera.Camera
import com.example.apotyk.databinding.ActivityShowProfileBinding
import com.example.apotyk.maps.MapActivity
import com.example.apotyk.model.User
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ShowProfile : AppCompatActivity() {
    private lateinit var binding: ActivityShowProfileBinding
    private lateinit var fotoProfil: ImageView
    private var queue: RequestQueue? = null
    private val loginPreference = "login"
    private var idUser:Long = 0
    var sharedPreferencesLogin: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesLogin = this?.getSharedPreferences(loginPreference, Context.MODE_PRIVATE)

        binding = ActivityShowProfileBinding.inflate(layoutInflater)
        queue = Volley.newRequestQueue(this)
        idUser = sharedPreferencesLogin!!.getLong("idUser",0)

        setContentView(binding.root)
        fotoProfil = findViewById(R.id.gambarProfil)
        fotoProfil.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            startActivity(intent)
        }

        getUserById(idUser)
        binding.btnEditUser.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("id", idUser)
            startActivity(intent)
        }
        binding.bottomNav.selectedItemId = R.id.menu_user
        binding.bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_obat-> {
                    val moveHome = Intent(this,HomeActivity::class.java)
                    startActivity(moveHome)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_riwayat -> {
                    val moveRiwayat = Intent(this,ShowRiwayat::class.java)
                    startActivity(moveRiwayat)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_user -> {

                }
                R.id.menu_exit -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_map -> {
                    val moveMap = Intent(this, MapActivity::class.java)
                    startActivity(moveMap)
                }
            }
        }
    }

    private fun getUserById(id: Long) {
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val json = JSONObject(response)
                var user = gson.fromJson(
                    json.getJSONArray("data")[0].toString(),
                    User::class.java
                )
                binding!!.etSessionUsername.setText(user.username)
                binding!!.etSessionEmail.setText(user.email)
                binding!!.etSessionTanggalLahir.setText(user.tanggal_lahir)
                    binding!!.etSessionNomorTelepon.setText(user.nomor_telepon)

                Toast.makeText(this@ShowProfile,"Data berhasil diambil", Toast.LENGTH_SHORT).show()

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
                        Toast.makeText(this@ShowProfile, e.message, Toast.LENGTH_SHORT).show()
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