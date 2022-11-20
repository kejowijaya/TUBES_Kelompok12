package com.example.apotyk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.apotyk.api.ObatApi
import com.example.apotyk.model.Obat
import com.google.gson.Gson

class FragmentObat : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_obat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        var adapter: RVObatAdapter? = null

        val rvObat : RecyclerView = view.findViewById(R.id.rv_obat)
        rvObat.layoutManager = layoutManager
        rvObat.setHasFixedSize(true)
        rvObat.adapter = adapter

    }
}