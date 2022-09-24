package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.entity.Obat

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
        val adapter : RVObatAdapter = RVObatAdapter(Obat.listOfObat)
        val rvDosen : RecyclerView = view.findViewById(R.id.rv_obat)
        rvDosen.layoutManager = layoutManager
        rvDosen.setHasFixedSize(true)
        rvDosen.adapter = adapter

    }
}