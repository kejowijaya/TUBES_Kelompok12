package com.example.apotyk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.apotyk.AddEditReservasiActivity
import com.example.apotyk.HomeActivity
import com.example.apotyk.R
import com.example.apotyk.ShowReservasi
import com.example.apotyk.model.Reservasi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class ReservasiAdapter (private var reservasiList: List<Reservasi>, context: Context) :
    RecyclerView.Adapter<ReservasiAdapter.ViewHolder>(), Filterable {

    private var filteredReservasiList: MutableList<Reservasi>
    private val context: Context

    init{
        filteredReservasiList = ArrayList(reservasiList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_reservasi, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredReservasiList.size
    }

    fun setReservasiList(reservasiList: Array<Reservasi>){
        this.reservasiList = reservasiList.toList()
        filteredReservasiList = reservasiList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservasi = filteredReservasiList[position]
        holder.tvTanggal.text = reservasi.tanggal
        holder.tvDokter.text = reservasi.dokter
        holder.tvSesi.text = reservasi.sesi
        holder.tvKeterangan.text = reservasi.keterangan

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data reservasi ini ?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if(context is ShowReservasi) reservasi.id?.let { it1 ->
                        context.deleteReservasi(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvReservasi.setOnClickListener{
            val i = Intent(context, AddEditReservasiActivity::class.java)
            i.putExtra("id", reservasi.id)
            if(context is ShowReservasi)
                context.startActivityForResult(i, ShowReservasi.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Reservasi> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(reservasiList)
                }else{
                    for(reservasi in reservasiList){
                        if(reservasi.tanggal.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(reservasi)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredReservasiList.clear()
                filteredReservasiList.addAll((filterResults.values as List<Reservasi>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvTanggal: TextView
        var tvSesi: TextView
        var tvDokter: TextView
        var tvKeterangan: TextView
        var btnDelete: ImageButton
        var cvReservasi: CardView

        init{
            tvTanggal = itemView.findViewById(R.id.tv_tanggal)
            tvSesi = itemView.findViewById(R.id.tv_sesi)
            tvDokter = itemView.findViewById(R.id.tv_dokter)
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvReservasi = itemView.findViewById(R.id.cv_reservasi)
        }
    }

}