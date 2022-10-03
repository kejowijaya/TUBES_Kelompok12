package com.example.apotyk

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apotyk.obat.Obat
import kotlinx.android.synthetic.main.activity_show_obat_adapter.view.*

class ShowObatAdapter (private val notes: ArrayList<Obat>, private val
listener: OnAdapterListener) :
    RecyclerView.Adapter<ShowObatAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            NoteViewHolder {
        return NoteViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_show_obat_adapter,parent, false)
        )
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position:
    Int) {
        val note = notes[position]
        holder.view.text_title.text = note.namaObat
        holder.view.text_title.setOnClickListener{
            listener.onClick(note)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(note)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(note)
        }
    }
    override fun getItemCount() = notes.size
    inner class NoteViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Obat>){
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(note: Obat)
        fun onUpdate(note: Obat)
        fun onDelete(note: Obat)
    }
}