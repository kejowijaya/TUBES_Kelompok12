package com.example.apotyk.api

class ReservasiApi {
    companion object {
        val BASE_URL = "https://apotyk.000webhostapp.com/apotyk_ci4/public/"

        val GET_ALL_URL = BASE_URL + "reservasi"
        val GET_BY_ID_URL = BASE_URL + "reservasi/"
        val ADD_URL = BASE_URL + "reservasi"
        val UPDATE_URL = BASE_URL + "reservasi/"
        val DELETE_URL = BASE_URL + "reservasi/"
    }
}