package vv.monika.funMaatee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vv.monika.funMaatee.databinding.ActivityLoginBinding
import vv.monika.funMaatee.retrofit_interface.ApiServiceInterface

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val credentialManager = CredentialManager.create(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.guestButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.continueButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if(currentUser == null){
                Toast.makeText(this, "Please Login first", Toast.LENGTH_SHORT).show()
            }
        }

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
        Log.d("Tag", "firebaseAuth with google IDTOKEN: ${idToken}")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Tag", "SignInwithCredentail:success")
                    startActivity(Intent(this, MainActivity::class.java))
//                    val user = auth.currentUser
//                    getting data from login
                    saveUserDataToPhpMySQL(idToken)

                } else {
                    Log.w("Tag", "signInWithCredential:failure", task.exception)

                }
            }

    }

    private fun saveUserDataToPhpMySQL(idToken: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        val json = JSONObject()
        json.put("uid", user?.uid ?: "")
        json.put("name", user?.displayName ?: "")
        json.put("email", user?.email ?: "")
        json.put("device_id", deviceId ?: "")
        json.put("idToken", idToken)

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json.toString()
        )

        Log.d("Tag", "Sending request to PHP...")
        Log.d("Tag", "Request JSON: ${json.toString()}")

        RetrofitBuilder.api.SaveUserData(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        // ✅ Read response body only once
                        val responseString = response.body()?.string() ?: ""
                        Log.d("Tag", "✅ SUCCESS Response: $responseString")

                        // ✅ Parse the JSON response
                        if (responseString.isNotEmpty()) {
                            try {
                                val responseJson = JSONObject(responseString)
                                val success = responseJson.getBoolean("success")

                                if (success) {
                                    val token = responseJson.getString("token")
                                    val message = responseJson.getString("message")
                                    Log.d("Tag", "✅ Login successful! Token: $token")

                                    // ✅ Save token to SharedPreferences
                                    val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                    sharedPref.edit().apply {
                                        putString("auth_token", token)
                                        putString("user_email", user?.email)
                                        putString("user_name", user?.displayName)
                                        apply()
                                    }
                                } else {
                                    val message = responseJson.getString("message")
                                    Log.e("Tag", "❌ Login failed: $message")
                                }
                            } catch (e: Exception) {
                                Log.e("Tag", "❌ Error parsing response JSON: ${e.message}")
                            }
                        }
                    } else {
                        // ✅ Handle error response
                        val errorString = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("Tag", "❌ HTTP Error ${response.code()}: $errorString")
                        Log.e("Tag", "❌ Response headers: ${response.headers()}")
                        Log.e("Tag", "❌ Request URL: ${call.request().url}")
                    }
                } catch (e: Exception) {
                    Log.e("Tag", "❌ Exception in onResponse: ${e.message}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Tag", "❌ Network Failure: ${t.message}")
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

    // Example for other API calls
    private fun makeAuthenticatedRequest() {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("auth_token", null)

        if (token != null) {
            // Add token to your API requests
            val headers = mapOf("Authorization" to "Bearer $token")
            // Make your API call...
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
