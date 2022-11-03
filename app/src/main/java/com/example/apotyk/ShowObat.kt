package com.example.apotyk

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apotyk.maps.MapActivity
import com.example.apotyk.user.Constant
import com.example.apotyk.obat.Obat
import com.example.apotyk.obat.ObatDB
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_show_obat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowObat : AppCompatActivity() {
    lateinit var mBundle: Bundle
    lateinit var bottomNav : BottomNavigationView
    val db by lazy { ObatDB(this) }
    lateinit var noteAdapter: ShowObatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBundle = intent.getBundleExtra("login")!!
        setContentView(R.layout.activity_show_obat)
        setupListener()
        setupRecyclerView()
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.selectedItemId = R.id.menu_riwayat
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_obat-> {
                    val moveHome = Intent(this,HomeActivity::class.java)
                    moveHome.putExtra("login", mBundle)
                    startActivity(moveHome)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_riwayat -> {

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
                }
                R.id.menu_map -> {
                    val moveMap = Intent(this, MapActivity::class.java)
                    startActivity(moveMap)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = ShowObatAdapter(arrayListOf(), object :
            ShowObatAdapter.OnAdapterListener{
            override fun onClick(note: Obat) {
                Toast.makeText(applicationContext, note.namaObat, Toast.LENGTH_SHORT).show()
                intentEdit(note.idObat,Constant.TYPE_READ)
            }
            override fun onUpdate(note: Obat) {
                intentEdit(note.idObat, Constant.TYPE_UPDATE)
            }
            override fun onDelete(note: Obat) {
                deleteDialog(note)
            }
        })
        list_note.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }
    private fun deleteDialog(note: Obat){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${note.namaObat}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.obatDao().deleteObat(note)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }
    //untuk load data yang tersimpan pada database yang sudah create data
    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.obatDao().getObats()
            Log.d("ShowUser","dbResponse: $notes")
            withContext(Dispatchers.Main){
                noteAdapter.setData(notes)
            }
        }
    }
    fun setupListener() {
        button_create.setOnClickListener{
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }
    //pick data dari Id yang sebagai primary key
    fun intentEdit(noteId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }
}