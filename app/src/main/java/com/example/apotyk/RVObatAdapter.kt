package com.example.apotyk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apotyk.databinding.RvItemObatBinding
import com.example.apotyk.model.Obat

class RVObatAdapter(private val data: Array<Obat>) : RecyclerView.Adapter<RVObatAdapter.viewHolder>() {
    private lateinit var binding: RvItemObatBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder{
        binding = RvItemObatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(val binding: RvItemObatBinding) :  RecyclerView.ViewHolder(binding.root){
        fun bind(obat: Obat) {
            binding.obat = obat
        }
    }

}
