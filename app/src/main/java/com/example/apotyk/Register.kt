package com.example.apotyk

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.getAbsolutePath
import com.example.apotyk.api.UserApi
import com.example.apotyk.databinding.ActivityMainBinding
import com.example.apotyk.databinding.ActivityRegisterBinding
import com.example.apotyk.model.User
import com.example.apotyk.user.UserDB
import com.google.gson.Gson
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Register : AppCompatActivity() {
    val db by lazy { UserDB(this) }

    private lateinit var binding: ActivityRegisterBinding
    private var CHANNEL_ID_1 = "channel_notification_1"
    private val notificationId2 = 101
    private var queue: RequestQueue? = null
    private val storageHelper = SimpleStorageHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        queue = Volley.newRequestQueue(this)
        setContentView(binding.root)
        setTitle("Register")
        createNotificationChannel()

        binding.btnRegister.setOnClickListener{
            var check = true

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val email = binding.etEmail.text.toString()
            val noTelp = binding.etNomorTelepon.text.toString()
            val tglLahir = binding.etTanggalLahir.text.toString()

            if(username.isEmpty() == true) {
                binding.etUsername.error = "Please enter username"
                check = false
            }
            if(password.isEmpty() == true) {
                binding.etPassword.error = "Please enter password"
                check = false
            }
            if(email.isEmpty() == true) {
                binding.etEmail.error = "Please enter email"
                check = false
            }
            if(noTelp.isEmpty() == true) {
                binding.etNomorTelepon.error = "Please enter nomor telepon"
                check = false
            }
            if(tglLahir.isEmpty() == true) {
                binding.etTanggalLahir.error = "Please enter tanggal lahir"
                check = false
            }

            if(check) {
                storageHelper.openFolderPicker(2)
            }

        }

        storageHelper.onFolderSelected = { requestCode, folder ->
            createUser()
            createPdf(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString(),
                binding.etEmail.text.toString(),
                binding.etNomorTelepon.text.toString(),
                binding.etTanggalLahir.text.toString(),
                folder.getAbsolutePath(this)
            )
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
                    Toasty.success(this@Register, "Register Berhasil !", Toast.LENGTH_SHORT, true).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                sendNotifiaction2()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toasty.error(
                        this@Register,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT, true
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

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    private fun createPdf(username: String, password: String, email: String, noTelp: String, tglLahir: String, uri: String) {

        val file = File(uri, "Data_Register_APOTYK.pdf")
        FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.apotyk)

        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val namapengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                Berikut adalah 
                Data Pengguna Aplikasi APOTYK
                """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)


        val width = floatArrayOf(100f, 100f)
        val table = Table(width)

        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Username")))
        table.addCell(Cell().add(Paragraph(username)))
        table.addCell(Cell().add(Paragraph("Password")))
        table.addCell(Cell().add(Paragraph(password)))
        table.addCell(Cell().add(Paragraph("Email")))
        table.addCell(Cell().add(Paragraph(email)))
        table.addCell(Cell().add(Paragraph("Nomor Telepon")))
        table.addCell(Cell().add(Paragraph(noTelp)))
        table.addCell(Cell().add(Paragraph("Tanggal Lahir")))
        table.addCell(Cell().add(Paragraph(tglLahir)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Waktu Pembuatan")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        val barcodeQRCode = BarcodeQRCode(
            """
                $username
                $password
                $email
                $noTelp
                $tglLahir
                ${LocalDate.now().format(dateTimeFormatter)}
                ${LocalTime.now().format(timeFormatter)}
                """.trimIndent()
        )
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
            HorizontalAlignment.CENTER)

        document.add(image)
        document.add(namapengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toasty.success(this, "PDF Created", Toast.LENGTH_SHORT).show()

    }
}