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
import com.example.apotyk.databinding.ActivityRegisterBinding
import com.example.apotyk.user.User
import com.example.apotyk.user.UserDB
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {
    val db by lazy { UserDB(this) }

    private lateinit var binding: ActivityRegisterBinding
    private var CHANNEL_ID_1 = "channel_notification_1"
    private val notificationId2 = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Register")
        createNotificationChannel()

        binding.btnRegister.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            sendNotifiaction2()
            if(binding.etUsername.text.toString().length > 0){
                CoroutineScope(Dispatchers.IO).launch {
                    db.userDao().addUser(
                        User((Math.random() * (10000 - 100 + 1)).toInt(), binding.etUsername.text.toString(),
                            binding.etTanggalLahir.text.toString(), binding.etEmail.text.toString(), binding.etNomorTelepon.text.toString(), binding.etPassword.text.toString())
                    )
                    finish()
                }

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