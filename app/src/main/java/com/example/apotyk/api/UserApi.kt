package com.example.apotyk.api

class UserApi {
    companion object{
        val BASE_URL = "https://apotyk.000webhostapp.com/apotyk_ci4/public/"

        val GET_ALL_URL = BASE_URL + "user"
        val GET_BY_ID_URL = BASE_URL + "user/"
        val ADD_URL = BASE_URL + "user"
        val UPDATE_URL = BASE_URL + "user/"
        val LOGIN_URL = BASE_URL + "login"
        val REGISTER_URL = BASE_URL + "register"
    }
}