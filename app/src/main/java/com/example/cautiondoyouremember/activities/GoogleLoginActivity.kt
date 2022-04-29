package com.example.cautiondoyouremember.activities

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.BuildConfig
import com.example.cautiondoyouremember.BuildConfig.WEB_CLIENT_ID
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.databinding.ActivityGoogleLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoogleLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 123
    val RC_ONE_TAP = 124
    private var showOneTapUI = true
    val TAG:String = "GoogleLogin"


    private val oneTapResult = registerForActivityResult(ActivityResultContracts.
    StartIntentSenderForResult()) { result ->

    try {
        val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken

        when {
            idToken!=null -> {
                val msg = "idToken: $idToken"
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).show()
                Log.d("OneTap", msg)
                updateUIOnSuccessfulLogin()
            } else -> {
                Log.d("one tap", "No ID token!")
                Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_INDEFINITE).show()
            }
        }
    } catch (e: ApiException) {
        when(e.statusCode) {
            CommonStatusCodes.CANCELED -> {
                Log.d("one tap", "One-tap dialog was closed.")
                // Don't re-prompt the user if the user cancels login
                Snackbar.make(binding.root, "One-tap dialog was closed.", Snackbar.LENGTH_INDEFINITE).show()
            }
            CommonStatusCodes.NETWORK_ERROR -> {
                Log.d("one tap", "One-tap encountered a network error.")
                // Try again or just ignore.
                Snackbar.make(binding.root, "One-tap encountered a network error.", Snackbar.LENGTH_INDEFINITE).show()
            }
            else -> {
                Log.d("one tap", "Couldn't get credential from result." +
                        " (${e.localizedMessage})")
                Snackbar.make(binding.root, "Couldn't get credential from result.\" +\n" +
                        " (${e.localizedMessage})", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

         firebaseAuth = Firebase.auth
         val user = firebaseAuth.currentUser

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(com.example.cautiondoyouremember.BuildConfig.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(com.example.cautiondoyouremember.BuildConfig.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        val loginButton = findViewById<ConstraintLayout>(R.id.loginBtn)
        loginButton.setOnClickListener {
            displaySignIn()
        }

    }

    private fun displaySignIn() {
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this) { result ->
            try {
                val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                oneTapResult.launch(ib)
            } catch (e:IntentSender.SendIntentException) {
                Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
            }
        }.addOnFailureListener(this) { e ->
            // No Google Accounts found. Just continue presenting the signed-out UI.
            displaySignUp()
            Log.d("btn click", e.localizedMessage!!)
        }
    }

    private fun displaySignUp() {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.d("btn click", e.localizedMessage!!)
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser!=null) {
            val homeActivityIntent = Intent(this,MainActivity::class.java)
            startActivity(homeActivityIntent)
        }
    }

    fun updateUIOnSuccessfulLogin() {
        val homeActivityIntent = Intent(this,MainActivity::class.java)
        startActivity(homeActivityIntent)
    }
}