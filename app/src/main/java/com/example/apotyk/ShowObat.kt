package com.example.apotyk

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apotyk.user.Constant
import com.example.apotyk.obat.Obat
import com.example.apotyk.obat.ObatDB
import kotlinx.android.synthetic.main.activity_show_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowObat : AppCompatActivity() {
    val db by lazy { ObatDB(this) }
    lateinit var noteAdapter: ShowObatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user)
        setupListener()
        setupRecyclerView()
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