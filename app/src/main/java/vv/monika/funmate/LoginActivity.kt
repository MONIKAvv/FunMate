package vv.monika.funmate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vv.monika.funmate.databinding.ActivityLoginBinding
import vv.monika.funmate.model.GoogleLoginRequest
import vv.monika.funmate.model.GoogleLoginResponse

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Configure Google SignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // ✅ Google SignIn button click
        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent) // no request code needed
            Log.d("Login", "ID Token: setonclick listener")
        }

        binding.continueButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // ✅ ActivityResultLauncher for Google SignIn
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d("Login", "ID Token: sSigninLauncher")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                val email = account.email
                val name = account.displayName

                Log.d("Login", "ID Token: $idToken, Email: $email, Name: $name")

                // send data to PHP backend
                if (idToken != null && email != null && name != null) {
                    sendGoogleLoginToServer(idToken, email, name)
                } else {
                    Log.d("Login", "ID Token: signinLauncher else part")
                    Toast.makeText(this, "Missing account info!", Toast.LENGTH_SHORT).show()
                }

            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // ✅ Send token to PHP backend using Retrofit
    private fun sendGoogleLoginToServer(idToken: String, email: String, name: String) {
        lifecycleScope.launch {
            try {
                Log.d("Login", "sendGoogleLogin to server")
                val request = GoogleLoginRequest(
                    idToken = idToken,
                    email = email,
                    name = name
                )

                val response = RetrofitBuilder.api.googleLogin(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("LoginResponse", body.toString())

                    if (body?.success == true) {
                        saveToken(this@LoginActivity, body.token ?: "")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            body?.message ?: "Login Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // ✅ Save token in SharedPreferences
    private fun saveToken(context: Context, token: String) {
        Log.d("Login", "ID Token: ssaveToken")
        context.getSharedPreferences("funmate_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("auth_token", token)
            .apply()
    }
}
