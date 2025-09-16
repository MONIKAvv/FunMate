package vv.monika.funmate.model

data class GoogleLoginResponse(
    val success: Boolean,
    val token: String?,
    val message: String?,
)
