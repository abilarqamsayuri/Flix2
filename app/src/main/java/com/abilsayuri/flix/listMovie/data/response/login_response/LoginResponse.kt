package com.abilsayuri.flix.listMovie.data.response.login_response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("username")
    var username: String? = null,

    @field:SerializedName("password")
    var password: String? = null
) : Parcelable
