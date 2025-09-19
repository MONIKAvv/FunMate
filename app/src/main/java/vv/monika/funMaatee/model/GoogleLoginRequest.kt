package vv.monika.funMaatee.model

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(
    @SerializedName("id_token") val idToken: String,
    val uid: String,
    val device_id: String,
    val email: String,
    val name: String,
)
