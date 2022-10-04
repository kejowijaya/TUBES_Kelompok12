package com.example.apotyk.entity

import com.example.apotyk.R


class Obat(var ID:String, var nama:String, var harga:String, var jenis:String, var gambar:Int) {
    companion object {
        @JvmField
        var listOfObat = arrayOf(
            Obat("1165", "Bodrex", "Rp. 8100", "Tablet", R.drawable.bodrex),
            Obat("1166", "OBH Combi Anak", "Rp. 20000", "Sirup", R.drawable.obhcombi),
            Obat("1167", "Insto Regular", "Rp. 15000", "Tetes", R.drawable.insto),
            Obat("1168", "Kalmicetine", "Rp. 17000", "Salep", R.drawable.kalmicetine),
            Obat("1169", "Inzana", "Rp. 1190", "Tablet", R.drawable.inzana),
            Obat("1170", "Konidin", "Rp. 2500", "Tablet", R.drawable.konidin),
            Obat("1171", "Cyclogynon", "Rp. 13800", "Tablet", R.drawable.cyclogynon),
            Obat("1172", "Proris Ibuprofen", "Rp. 22000", "Sirup", R.drawable.proris),
            Obat("1173", "Komix", "Rp. 10000", "Sirup", R.drawable.komix),
            Obat("1174", "Promag", "Rp. 9500", "Tablet", R.drawable.promag)
        )
    
    }
}