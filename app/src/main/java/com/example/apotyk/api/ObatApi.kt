package com.example.apotyk.api

class ObatApi {

    companion object {
        val BASE_URL = "http://192.168.228.84:8081/apotyk/ci4-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "obat"
        val GET_BY_ID_URL = BASE_URL + "obat/"
        val ADD_URL = BASE_URL + "obat"
        val UPDATE_URL = BASE_URL + "obat/"
        val DELETE_URL = BASE_URL + "obat/"
    }

}