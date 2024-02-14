package com.abilsayuri.flix.local

import android.content.Context
import com.abilsayuri.flix.listMovie.data.response.login_response.LoginResponse

class UserPreferences(context: Context) {

    companion object {
        private val PREF_NAME = "user_preferences"
        private val USERNAME = "username"
        private val PASSWORD = "password"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // fungsi untuk menyimpan data
    fun setUser(responseLogin: LoginResponse) {
        val set = preferences.edit()
        set.putString(USERNAME, responseLogin.username)
        set.putString(PASSWORD, responseLogin.password)

        set.apply()
    }

    // fungsi untuk mengambil data
    fun getUser(): LoginResponse {
        val data = LoginResponse()
        data.username = preferences.getString(USERNAME, null)
        data.password = preferences.getString(PASSWORD, null)

        return data
    }

}