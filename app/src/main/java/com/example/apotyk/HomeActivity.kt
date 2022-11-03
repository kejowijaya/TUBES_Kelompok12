package com.example.apotyk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.example.apotyk.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.maps.MapActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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

}