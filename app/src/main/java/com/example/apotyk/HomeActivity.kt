package com.example.apotyk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.apotyk.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class HomeActivity : AppCompatActivity() {
    lateinit var mBundle: Bundle
    lateinit var bottomNav : BottomNavigationView
    private var binding: ActivityMainBinding? = null
    private var CHANNEL_ID_2 = "channel_notification_2"
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(FragmentObat())
        createNotificationChannel()
        mBundle = intent.getBundleExtra("login")!!
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_obat-> {
                    loadFragment(FragmentObat())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_riwayat -> {
                    startActivity(Intent(this,ShowUser::class.java))
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_user -> {
                    val moveProfile = Intent(this,ShowProfile::class.java)
                    moveProfile.putExtra("login", mBundle)
                    startActivity(moveProfile)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_exit -> {
                    val intent = Intent(this, SplashScreen::class.java)
                    startActivity(intent)
                    sendNotifiaction2()
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
                .bigText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s," +
                        " when an unknown printer took a galley of type and scrambled it to make a type " +
                        "specimen book. It has survived not only five centuries, but also the leap into " +
                        "electronic typesetting, remaining essentially unchanged. " +
                        "It was popularised in the 1960s with the release of Letraset sheets containing " +
                        "Lorem Ipsum passages, and more recently with desktop publishing software like Aldus " +
                        "PageMaker including versions of Lorem Ipsum."))

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }

}