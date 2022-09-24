package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.login.entity.Obat

class RVObatAdapter(private val data: Array<Obat>) : RecyclerView.Adapter<RVObatAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_obat, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.tvNamaDosen.text = "Rp ${currentItem.harga.toString()}"
        holder.tvDetailDosen.text = currentItem.nama
        holder.tvGambarObat.setImageResource(currentItem.gambar)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        val tvNamaDosen : TextView = itemView.findViewById(R.id.tv_nama_dosen)
        val tvDetailDosen : TextView = itemView.findViewById(R.id.tv_details_dosen)
        val tvGambarObat : ImageView = itemView.findViewById(R.id.gambar_obat)
    }

}
