package vv.monika.funmate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.credentials.IdToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import vv.monika.funmate.databinding.ActivityLoginBinding
import vv.monika.funmate.model.GoogleLoginRequest
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        configure google signin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        binding.continueButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

//        Retrofit implementation for login


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                val email = account.email
                val name = account.displayName

//                send this data to php backend
                sendGoogleLoginToServer(idToken!!, email!!, name!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Goodle Sign-In failed : ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun sendGoogleLoginToServer(
        idToken: String,
        email: String,
        name: String
    ) {
        lifecycleScope.launch {
            try {
                val response =
                    RetrofitInstance.api.googleLogin(GoogleLoginRequest(idToken, email, name))

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        saveToken(this@LoginActivity, body.token?: "")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            body?.message ?: "Login Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(this, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun saveToken(context: Context, token:String) {

        context.getSharedPreferences("funmate_prefs", Context.MODE_PRIVATE)
            .edit().putString("auth_token", token)
            .apply()    }
}


//login -> googlepicker open -> email select -> token send -> google/db -> google token id --> app --> db store krna hai,