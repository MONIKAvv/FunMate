package vv.monika.funMaatee.model

data class GoogleLoginResponse(
    val success: Boolean,
    val token: String?,
    val message: String?,
)
