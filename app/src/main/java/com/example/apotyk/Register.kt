package com.example.apotyk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.api.UserApi
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.databinding.ActivityRegisterBinding
import com.example.apotyk.model.User
import com.example.apotyk.user.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class Register : AppCompatActivity() {
    val db by lazy { UserDB(this) }

    private lateinit var binding: ActivityRegisterBinding
    private var CHANNEL_ID_1 = "channel_notification_1"
    private val notificationId2 = 101
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        queue = Volley.newRequestQueue(this)
        setContentView(binding.root)
        setTitle("Register")
        createNotificationChannel()

        binding.btnRegister.setOnClickListener{
            var check = true
            if(binding.etUsername.text?.isEmpty() == true) {
                binding.etUsername.error = "Please enter username"
                check = false
            }
            if(binding.etPassword.text?.isEmpty() == true) {
                binding.etPassword.error = "Please enter password"
                check = false
            }
            if(binding.etEmail.text?.isEmpty() == true) {
                binding.etEmail.error = "Please enter email"
                check = false
            }
            if(binding.etNomorTelepon.text?.isEmpty() == true) {
                binding.etNomorTelepon.error = "Please enter nomor telepon"
                check = false
            }
            if(binding.etTanggalLahir.text?.isEmpty() == true) {
                binding.etTanggalLahir.error = "Please enter tanggal lahir"
                check = false
            }

            if(check) {
                createUser()
            }

        }

    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(
                CHANNEL_ID_1,
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
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.apotyk)

        val broadcastIntent: Intent = Intent(this, Register::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, broadcastIntent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Selamat Datang")
            .setContentText("Selamat Anda telah berhasil register")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap))
            .addAction(R.mipmap.ic_launcher, "Bikin Akun Lagi", pendingIntent)
            .setColor(Color.BLUE)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }

    private fun createUser() {

        val user = User(
            binding.etUsername.text.toString(),
            binding.etPassword.text.toString(),
            binding.etEmail.text.toString(),
            binding.etTanggalLahir.text.toString(),
            binding.etNomorTelepon.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, User::class.java)

                if (user != null)
                    Toast.makeText(this@Register, "Register Berhasil !", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                sendNotifiaction2()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@Register,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@Register, e.message, Toast.LENGTH_SHORT).show()
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
    }
}