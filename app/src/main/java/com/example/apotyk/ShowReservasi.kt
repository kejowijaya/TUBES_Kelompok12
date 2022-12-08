package com.example.apotyk

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.adapter.ReservasiAdapter
import com.example.apotyk.api.ReservasiApi
import com.example.apotyk.maps.MapActivity
import com.example.apotyk.model.Reservasi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ShowReservasi : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    private var queue: RequestQueue? = null
    private var srReservasi: SwipeRefreshLayout? = null
    private var adapter: ReservasiAdapter? = null
    private var svReservasi: SearchView? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_reservasi)

        queue = Volley.newRequestQueue(this)
        srReservasi = findViewById(R.id.sr_reservasi)
        svReservasi = findViewById(R.id.sv_reservasi)

        srReservasi?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener {
            allReservasi()
        })
        svReservasi?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter!!.filter.filter(p0)
                return false
            }
        })

        val fabAdd = findViewById<Button>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val i = Intent(this@ShowReservasi,  AddEditReservasiActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_reservasi)
        adapter = ReservasiAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allReservasi()
        
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView

        bottomNav.selectedItemId = R.id.menu_riwayat
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_obat-> {
                    val moveHome = Intent(this,HomeActivity::class.java)
                    startActivity(moveHome)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_riwayat -> {

                }
                R.id.menu_user -> {
                    val moveProfile = Intent(this,ShowProfile::class.java)
                    startActivity(moveProfile)
                    return@setOnNavigationItemReselectedListener
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

    private fun allReservasi(){
        srReservasi!!.isRefreshing = true
        val stringRequest : StringRequest = object:
            StringRequest(Method.GET, ReservasiApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val json = JSONObject(response)
                var reservasi : Array<Reservasi> = gson.fromJson(
                    json.getJSONArray("data").toString(),
                    Array<Reservasi>::class.java
                )

                adapter!!.setReservasiList(reservasi)
                adapter!!.filter.filter(svReservasi!!.query)
                srReservasi!!.isRefreshing = false

                if(!reservasi.isEmpty())
                    Toasty.success(this@ShowReservasi, "Data berhasil diambil", Toast.LENGTH_SHORT, true).show()
                else
                    Toasty.warning(this@ShowReservasi, "Data Kosong!", Toast.LENGTH_SHORT, true).show()

            }, Response.ErrorListener { error ->
                srReservasi!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(this@ShowReservasi, errors.getString("message"), Toast.LENGTH_SHORT, true).show()
                } catch (e: Exception){
                    Toast.makeText(this@ShowReservasi, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteReservasi(id: Long){
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, ReservasiApi.DELETE_URL+id, Response.Listener { response ->

                val gson = Gson()
                var reservasi = gson.fromJson(response, Reservasi::class.java)
                if(reservasi != null)
                    Toasty.success(this@ShowReservasi, "Data Berhasil Dihapus", Toast.LENGTH_SHORT, true).show()

                allReservasi()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(this@ShowReservasi, errors.getString("message"), Toast.LENGTH_SHORT, true).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@ShowReservasi, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == HomeActivity.LAUNCH_ADD_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                allReservasi()
            }
        }
    }
}