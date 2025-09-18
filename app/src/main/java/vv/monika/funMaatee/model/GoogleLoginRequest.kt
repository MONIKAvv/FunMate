package vv.monika.funMaatee.model

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(
    @SerializedName("id_token") val idToken: String,
    val email: String,
    val name: String,
)
