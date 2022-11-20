package com.example.apotyk

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
import com.example.apotyk.model.Obat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class RVObatAdapter(private var obatList: List<Obat>, context: Context) :
    RecyclerView.Adapter<RVObatAdapter.ViewHolder>(), Filterable {
    
    private var filteredObatList: MutableList<Obat>
    private val context: Context

    init{
        filteredObatList = ArrayList(obatList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_obat, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredObatList.size
    }

    fun setObatList(obatList: Array<Obat>){
        this.obatList = obatList.toList()
        filteredObatList = obatList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obat = filteredObatList[position]
        holder.tvNama.text = obat.nama
        holder.tvJenis.text = obat.jenis
        holder.tvHarga.text = obat.harga

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data obat ini ?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if(context is HomeActivity) obat.id?.let { it1 ->
                        context.deleteObat(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvObat.setOnClickListener{
            val i = Intent(context, AddEditObatActivity::class.java)
            i.putExtra("id", obat.id)
            if(context is HomeActivity)
                context.startActivityForResult(i, HomeActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Obat> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(obatList)
                }else{
                    for(obat in obatList){
                        if(obat.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(obat)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredObatList.clear()
                filteredObatList.addAll((filterResults.values as List<Obat>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNama: TextView
        var tvHarga: TextView
        var tvJenis: TextView
        var btnDelete: ImageButton
        var cvObat: CardView

        init{
            tvNama = itemView.findViewById(R.id.tv_nama)
            tvHarga = itemView.findViewById(R.id.tv_harga)
            tvJenis = itemView.findViewById(R.id.tv_jenis)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvObat = itemView.findViewById(R.id.cv_obat)
        }
    }

}
