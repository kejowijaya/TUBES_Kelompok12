package com.example.apotyk

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.model.Obat
import com.example.apotyk.api.ObatApi
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.maps.MapActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class HomeActivity : AppCompatActivity() {
    lateinit var mBundle: Bundle
    lateinit var bottomNav : BottomNavigationView
    private var binding: ActivityMainBinding? = null
    private var CHANNEL_ID_2 = "channel_notification_2"
    private val notificationId2 = 102
    private lateinit var tombolTambah: FloatingActionButton
    private var queue: RequestQueue? = null
    private var srObat: SwipeRefreshLayout? = null
    private var adapter: RVObatAdapter? = null
    private var svObat: SearchView? = null
    private var layoutLoading: LinearLayout? = null


    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srObat = findViewById(R.id.sr_obat)
        svObat = findViewById(R.id.sv_obat)

        srObat?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allObat() })
        svObat?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter!!.filter.filter(p0)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val i = Intent(this@HomeActivity,  AddEditObatActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_obat)
        adapter = RVObatAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allObat()

        createNotificationChannel()

        mBundle = intent.getBundleExtra("login")!!
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        tombolTambah = findViewById(R.id.fab_add)
        tombolTambah.setOnClickListener {
            val move = Intent(this, AddEditObatActivity::class.java)
            move.putExtra("login", mBundle)
            startActivity(move)
        }
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_obat-> {

                }
                R.id.menu_riwayat -> {
                    val moveRiwayat = Intent(this,ShowObat::class.java)
                    moveRiwayat.putExtra("login", mBundle)
                    startActivity(moveRiwayat)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_user -> {
                    val moveProfile = Intent(this,ShowProfile::class.java)
                    moveProfile.putExtra("login", mBundle)
                    startActivity(moveProfile)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_exit -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    sendNotifiaction2()
                }

                R.id.menu_map -> {
                    val moveMap = Intent(this, MapActivity::class.java)
                    startActivity(moveMap)
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(
                CHANNEL_ID_2,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotifiaction2() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_2)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Terima Kasih")
            .setContentText("Silahkan datang kembali dan sehat selalu. Anda sehat kami puas")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Silahkan datang kembali dan sehat selalu. Anda sehat kami puas. " +
                        "Kesehatan adalah keadaan harmoni dari tubuh, pikiran, dan jiwa. " +
                        "Nilai berhubungan dengan emosi kita, " +
                        "sama seperti kita mempraktikkan kebersihan fisik untuk menjaga kesehatan fisik kita, " +
                        "kita perlu mengamati kebersihan emosional untuk menjaga pikiran dan sikap yang sehat. " +
                        "Kesehatanlah yang merupakan kekayaan sejati, bukan kepingan emas dan perak. " +
                        "Merawat diri sendiri bukanlah hal yang egois. Karena ini penting untuk kelangsungan hidup dan kesejahteraan Anda. "))

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }

    private fun allObat(){
        srObat!!.isRefreshing = true
        val stringRequest : StringRequest = object:
            StringRequest(Method.GET, ObatApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val json = JSONObject(response)
                var obat : Array<Obat> = gson.fromJson(
                    json.getJSONArray("data").toString(),
                    Array<Obat>::class.java
                )

                adapter!!.setObatList(obat)
                adapter!!.filter.filter(svObat!!.query)
                srObat!!.isRefreshing = false

                if(!obat.isEmpty())
                    Toast.makeText(this@HomeActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@HomeActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener { error ->
                srObat!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@HomeActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteObat(id: Long){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, ObatApi.DELETE_URL+id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var obat = gson.fromJson(response, Obat::class.java)
                if(obat != null)
                    Toast.makeText(this@HomeActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

                allObat()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@HomeActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
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
        if(requestCode == LAUNCH_ADD_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                allObat()
            }
        }
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.INVISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

}