package com.example.apotyk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.api.ReservasiApi
import com.example.apotyk.model.Reservasi
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditReservasiActivity : AppCompatActivity() {
    companion object {
        private val SESI_LIST = arrayOf("1 (08.00-10.00)", "2 (11.00 - 13.00)", "3 (14.00 - 16.00)", "4 (17.00 - 19.00)")
        private val DOKTER_LIST = arrayOf("Dr. A", "Dr. B", "Dr. C", "Dr. D")
    }
    private var etTanggal: EditText? = null
    private var edSesi: AutoCompleteTextView? = null
    private var edDokter: AutoCompleteTextView? = null
    private var etKeterangan: EditText? = null
    private var queue: RequestQueue? = null
    private var CHANNEL_ID_2 = "channel_notification_2"
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_reservasi)

        queue = Volley.newRequestQueue(this)
        etTanggal = findViewById(R.id.et_tanggal)
        edSesi = findViewById(R.id.ed_sesi)
        edDokter = findViewById(R.id.ed_dokter)
        etKeterangan = findViewById(R.id.et_keterangan)

        setExposedDropdownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }

        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        createNotificationChannel()
        val id = intent.getLongExtra("id", -1)
        if (id == -1L) {
            tvTitle.setText("Buat Janji Dokter")
            btnSave.setOnClickListener { createReservasi() }
        } else {
            tvTitle.setText("Edit Janji Dokter")
            getReservasiById(id)
            btnSave.setOnClickListener { updateReservasi(id) }
        }
    }
    
    private fun createReservasi() {

        val reservasi = Reservasi(
            etTanggal!!.text.toString(),
            edSesi!!.text.toString(),
            edDokter!!.text.toString(),
            etKeterangan!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, ReservasiApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var reservasi = gson.fromJson(response, Reservasi::class.java)

                if (reservasi != null)
                    Toasty.success(this@AddEditReservasiActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT, true).show()

                sendNotification()
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(
                        this@AddEditReservasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT, true
                    ).show()
                } catch (e: Exception) {
                    Toasty.error(this@AddEditReservasiActivity, e.message.toString(), Toast.LENGTH_SHORT, true).show()
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
                    params["tanggal"] = reservasi.tanggal
                    params["sesi"] = reservasi.sesi
                    params["dokter"] = reservasi.dokter
                    params["keterangan"] = reservasi.keterangan
                    return params
                }
            }

        queue!!.add(stringRequest)
    }
    private fun updateReservasi(id: Long) {
        val reservasi = Reservasi(
            etTanggal!!.text.toString(),
            edSesi!!.text.toString(),
            edDokter!!.text.toString(),
            etKeterangan!!.text.toString(),
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, ReservasiApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var reservasi = gson.fromJson(response, Reservasi::class.java)

                if(reservasi != null)
                    Toasty.success(this@AddEditReservasiActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT, true).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(
                        this@AddEditReservasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT, true
                    ).show()
                } catch (e: Exception) {
                    Toasty.error(this@AddEditReservasiActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
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
                params["tanggal"] = reservasi.tanggal
                params["sesi"] = reservasi.sesi
                params["dokter"] = reservasi.dokter
                params["keterangan"] = reservasi.keterangan
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    fun setExposedDropdownMenu(){
        val adapterSesi: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.item_list, SESI_LIST)
        edSesi!!.setAdapter(adapterSesi)

        val adapterDokter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.item_list, DOKTER_LIST)
        edDokter!!.setAdapter(adapterDokter)
    }

    private fun getReservasiById(id: Long) {
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, ReservasiApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val json = JSONObject(response)
                var reservasi = gson.fromJson(
                    json.getJSONArray("data")[0].toString(),
                    Reservasi::class.java
                )

                etTanggal!!.setText(reservasi.tanggal)
                edSesi!!.setText(reservasi.sesi)
                edDokter!!.setText(reservasi.dokter)
                etKeterangan!!.setText(reservasi.keterangan)

                setExposedDropdownMenu()
                Toasty.success(this@AddEditReservasiActivity,"Data berhasil diambil", Toast.LENGTH_SHORT, true).show()
            },
                Response.ErrorListener{ error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toasty.error(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT, true
                        ).show()
                    } catch (e: Exception){
                        Toasty.error(this@AddEditReservasiActivity, e.message.toString(), Toast.LENGTH_SHORT, true).show()
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

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_2).apply {
            setContentTitle("Add Data")
            setSmallIcon(R.drawable.ic_baseline_notifications_24)
            setPriority(NotificationCompat.PRIORITY_HIGH)
        }

        var progress = 0
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(100, 0, false)
            notify(notificationId2, builder.build())

            Thread(kotlinx.coroutines.Runnable {
                while (progress < 100) {
                    Thread.sleep(250)
                    progress += 10
                    runOnUiThread {
                        builder.setProgress(100, progress, false)
                        builder.setContentText("Add data in progress....")
                        notify(notificationId2, builder.build())
                        if (progress == 100) {
                            builder.setContentText("Add data complete")
                            builder.setProgress(0, 0, false)
                            notify(notificationId2, builder.build())
                        }
                    }
                }
            }).start()

        }
    }
}