package com.example.apotyk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.apotyk.user.Constant
import com.example.apotyk.obat.Obat
import com.example.apotyk.obat.ObatDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class EditRiwayatActivity : AppCompatActivity() {
    private var CHANNEL_ID_2 = "channel_notification_2"
    private val notificationId2 = 102
    val db by lazy { ObatDB(this) }
    private var noteId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()
        createNotificationChannel()
//
        Toast.makeText(this, noteId.toString(),Toast.LENGTH_SHORT).show()
    }
    fun setupView(){
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getNote()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getNote()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {
            sendNotification()
            CoroutineScope(Dispatchers.IO).launch {
                sendNotification()
                db.obatDao().addObat(
                    Obat(0,edit_title.text.toString(),
                        edit_note.text.toString())
                )

                finish()

            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.obatDao().updateObat(
                    Obat(noteId, edit_title.text.toString(),
                        edit_note.text.toString())
                )
                finish()
            }
        }
    }
    fun getNote() {
        noteId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.obatDao().getObat(noteId)[0]
            edit_title.setText(notes.namaObat)
            edit_note.setText(notes.jumlahObat)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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

            Thread(Runnable{
                while(progress < 100){
                    Thread.sleep(250)
                    progress += 10
                    runOnUiThread{
                        builder.setProgress(100, progress, false)
                        builder.setContentText("Add data in progress....")
                        notify(notificationId2, builder.build())
                        if(progress == 100){
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