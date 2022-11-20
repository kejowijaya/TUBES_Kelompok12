package com.example.apotyk.api

class ObatApi {

    companion object {
        val BASE_URL = "https://apotyk.herokuapp.com/"

        val GET_ALL_URL = BASE_URL + "obat"
        val GET_BY_ID_URL = BASE_URL + "obat/"
        val ADD_URL = BASE_URL + "obat"
        val UPDATE_URL = BASE_URL + "obat/"
        val DELETE_URL = BASE_URL + "obat/"
    }

}