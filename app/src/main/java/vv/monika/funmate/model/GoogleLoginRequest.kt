package vv.monika.funmate.model

data class GoogleLoginRequest(
    val idToken: String,
    val email: String,
    val name: String,
)
