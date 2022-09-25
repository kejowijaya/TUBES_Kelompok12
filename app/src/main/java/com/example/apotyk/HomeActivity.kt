package com.example.apotyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class HomeActivity : AppCompatActivity() {
    lateinit var mBundle: Bundle
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(FragmentObat())
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

}