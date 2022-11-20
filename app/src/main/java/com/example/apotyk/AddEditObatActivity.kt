package com.example.apotyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apotyk.api.ObatApi
import com.example.apotyk.model.Obat
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditObatActivity : AppCompatActivity() {
    companion object {
        private val JENIS_LIST = arrayOf("Tablet", "Kapsul", "Sirup", "Salep", "Cream", "Lainnya")
    }
    private var etNama: EditText? = null
    private var edJenis: AutoCompleteTextView? = null
    private var etHarga: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_obat)
        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        edJenis = findViewById(R.id.ed_jenis)
        etHarga = findViewById(R.id.et_harga)
        layoutLoading = findViewById(R.id.layout_loading)
        val adapterJenis = ArrayAdapter(this, R.layout.item_list, JENIS_LIST)
        edJenis!!.setAdapter(adapterJenis)
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)

        if (id == -1L) {
            tvTitle.setText("Tambah Obat")
            btnSave.setOnClickListener { createObat() }
        } else {
            tvTitle.setText("Edit Obat")
            getObatById(id)
            btnSave.setOnClickListener { updateObat(id) }
        }
    }
    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags (WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
    private fun createObat() {
        setLoading(true)

        val obat = Obat(
            etNama!!.text.toString(),
            edJenis!!.text.toString(),
            etHarga!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, ObatApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var obat = gson.fromJson(response, Obat::class.java)

                if (obat != null)
                    Toast.makeText(this@AddEditObatActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditObatActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditObatActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    params["nama"] = obat.nama
                    params["jenis"] = obat.jenis
                    params["harga"] = obat.harga
                    return params
                }
            }

        queue!!.add(stringRequest)
    }
    private fun updateObat(id: Long) {
        setLoading(true)

        val obat = Obat(
            etNama!!.text.toString(),
            edJenis!!.text.toString(),
            etHarga!!.text.toString(),
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, ObatApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var obat = gson.fromJson(response, Obat::class.java)

                if(obat != null)
                    Toast.makeText(this@AddEditObatActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditObatActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditObatActivity, e.message, Toast.LENGTH_SHORT).show()
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
                params["nama"] = obat.nama
                params["jenis"] = obat.jenis
                params["harga"] = obat.harga
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    fun setExposedDropdownMenu(){
        val adapterJenis: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.item_list, JENIS_LIST)
        edJenis!!.setAdapter(adapterJenis)
    }

    private fun getObatById(id: Long) {

        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ObatApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val json = JSONObject(response)
                var obat = gson.fromJson(
                    json.getJSONArray("data")[0].toString(),
                    Obat::class.java
                )

                etNama!!.setText(obat.nama)
                edJenis!!.setText(obat.jenis)
                etHarga!!.setText(obat.harga)

                setExposedDropdownMenu()
                Toast.makeText(this@AddEditObatActivity,"Data berhasil diambil", Toast.LENGTH_SHORT).show()
                setLoading(false)
            },
                Response.ErrorListener{ error ->
                    setLoading(false)
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@AddEditObatActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
}