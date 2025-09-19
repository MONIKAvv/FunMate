package vv.monika.funMaatee

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vv.monika.funMaatee.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    private val credentialManager = CredentialManager.create(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        // ✅ Configure Google SignIn
        val gso = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id)) // from google-services.json
            .setFilterByAuthorizedAccounts(false)
            .build()
//        create credential manager request
        val request = GetCredentialRequest.Builder().addCredentialOption(
            gso
        ).build()

        // ✅ Google SignIn button click
        binding.googleButton.setOnClickListener {


            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = this@LoginActivity
                    )
                    handleSignIn(result.credential)  // yaha pass hoga credential
                } catch (e: Exception) {
                    Log.e("Tag", "Sign-in failed: ${e.message}")
                }
            }

//            signInLauncher.launch(signInIntent) // no request code needed
            Log.d("Tag", "ID Token: setonclick listener")
        }

//        binding.continueButton.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
    }

    private fun handleSignIn(credential: androidx.credentials.Credential) {
//checking if credential is googleid
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//            CREATE google id tokem
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
//            sign in to firebase
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w("Tag", "Credential is not of type Google ID!")
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Tag", "SignInwithCredentail:success")
                    startActivity(Intent(this, MainActivity::class.java))
//                    val user = auth.currentUser
//                    getting data from login
                    saveUserDataToPhpMySQL()
//                    val user = FirebaseAuth.getInstance().currentUser
//                    user?.let {
//                        val uid = it.uid
//                        val name = it.displayName
//                        val email = it.email
//                        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//                    }


                } else {
                    // If sign in fails, display a message to the user
                    Log.w("Tag", "signInWithCredential:failure", task.exception)

                }
            }

    }

    private fun saveUserDataToPhpMySQL() {

        val user = FirebaseAuth.getInstance().currentUser
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        RetrofitBuilder.api.googleLogin(
            user!!.uid,
            user.displayName,
            user.email,
            deviceId
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d("Tag", "Server Response: ${response.body()}")
                } else {
                    Log.e("Tag", "Error Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Tag", "Error: ${t.message}")
            }
        })


    }

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // When a user signs out, clear the current user credential state from all credential providers.
        lifecycleScope.launch {
            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)

            } catch (e: ClearCredentialException) {
                Log.e("Tag", "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}
