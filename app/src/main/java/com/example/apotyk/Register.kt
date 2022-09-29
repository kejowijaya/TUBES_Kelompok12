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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.user.User
import com.example.apotyk.user.UserDB
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var email : TextInputEditText
    private lateinit var tanggalLahir : TextInputEditText
    private lateinit var nomorTelepon : TextInputEditText
    private lateinit var btnRegister : Button

    private var binding: ActivityMainBinding? = null
    private var CHANNEL_ID_1 = "channel_notification_1"
    private val notificationId2 = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTitle("Register")
        createNotificationChannel()
        username = findViewById(R.id.etUsername)
        password = findViewById(R.id.etPassword)
        email = findViewById(R.id.etEmail)
        nomorTelepon = findViewById(R.id.etNomorTelepon)
        tanggalLahir = findViewById(R.id.etTanggalLahir)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",username.text.toString())
            mBundle.putString("password",password.text.toString())
            mBundle.putString("tanggalLahir",tanggalLahir.text.toString())
            mBundle.putString("email",email.text.toString())
            mBundle.putString("nomorTelepon",nomorTelepon.text.toString())
            intent.putExtra("register", mBundle)
            sendNotifiaction2()
            if(username.text.toString().length == 0){
                CoroutineScope(Dispatchers.IO).launch {
                    db.userDao().addUser(
                        User(1, "a",
                            "a", "a", "a", "a")
                    )
                    finish()
                }

            }

            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().addUser(
                    User((Math.random() * (10000 - 100 + 1)).toInt(), username.text.toString(),
                        password.text.toString(), email.text.toString(), nomorTelepon.text.toString(), password.text.toString())
                )
                finish()
            }

            startActivity(intent)
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
        val intent = Intent(this, Register::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


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
}