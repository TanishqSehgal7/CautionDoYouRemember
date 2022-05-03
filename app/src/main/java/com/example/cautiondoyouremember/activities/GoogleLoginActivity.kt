package com.example.cautiondoyouremember.activities

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.SavedPreference
import com.example.cautiondoyouremember.databinding.ActivityGoogleLoginBinding
import com.example.cautiondoyouremember.User.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

/*
THIS FILE ALSO CONTAINS CODE FOR ONE TAP GOOGLE LOGIN
BUT WE ARE NOT USING THAT HERE.
WE ARE USING GOOGLE LOGIN WITH FIREBASE HERE, SO PLEASE
IGNORE THE COMMENTED CODE
 */

class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoogleLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
//    private lateinit var signUpRequest: BeginSignInRequest
//    private lateinit var googleSignInClient: GoogleSignInClient
//    val RC_SIGN_IN = 123
//    val RC_ONE_TAP = 124
//    private var showOneTapUI = true
    val TAG:String = "GoogleLogin"
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference

//    private val oneTapResult = registerForActivityResult(ActivityResultContracts.
//    StartIntentSenderForResult()) { result ->
//
//    try {
//        val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
//        val idToken = credential.googleIdToken
//
//        when {
//            idToken!=null -> {
//                val msg = "idToken: $idToken"
//                Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).show()
//                Log.d("OneTap", msg)
//                updateUIOnSuccessfulLogin()
//            } else -> {
//                Log.d("one tap", "No ID token!")
//                Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_INDEFINITE).show()
//            }
//        }
//    } catch (e: ApiException) {
//        when(e.statusCode) {
//            CommonStatusCodes.CANCELED -> {
//                Log.d("one tap", "One-tap dialog was closed.")
//                // Don't re-prompt the user if the user cancels login
//                Snackbar.make(binding.root, "One-tap dialog was closed.", Snackbar.LENGTH_INDEFINITE).show()
//            }
//            CommonStatusCodes.NETWORK_ERROR -> {
//                Log.d("one tap", "One-tap encountered a network error.")
//                // Try again or just ignore.
//                Snackbar.make(binding.root, "One-tap encountered a network error.", Snackbar.LENGTH_INDEFINITE).show()
//            }
//            else -> {
//                Log.d("one tap", "Couldn't get credential from result." +
//                        " (${e.localizedMessage})")
//                Snackbar.make(binding.root, "Couldn't get credential from result.\" +\n" +
//                        " (${e.localizedMessage})", Snackbar.LENGTH_INDEFINITE).show()
//                }
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

         firebaseAuth = Firebase.auth
//         val user = firebaseAuth.currentUser

        binding.loginActivityBackButton.setOnClickListener {
            finishAffinity()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

//        oneTapClient = Identity.getSignInClient(this)
//        signUpRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(com.example.cautiondoyouremember.BuildConfig.WEB_CLIENT_ID)
//                    // Show all accounts on the device.
//                    .setFilterByAuthorizedAccounts(false)
//                    .build())
//            .build()
//        signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(com.example.cautiondoyouremember.BuildConfig.WEB_CLIENT_ID)
//                    // Show all accounts on the device.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            .build()

        val loginButton = findViewById<ConstraintLayout>(R.id.loginBtn)
        loginButton.setOnClickListener {
//            displaySignIn()
            signInGoogle()
        }

    }

    private  fun signInGoogle(){
        val signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                SavedPreference.setEmail(this,account.email.toString())
                SavedPreference.setUsername(this,account.displayName.toString())
                SavedPreference.setUserId(this,account.id.toString())
        val homeActivityIntent = Intent(this,FaceDetectorAndCaptureActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(homeActivityIntent)
                val acct = GoogleSignIn.getLastSignedInAccount(this)
                if (acct != null) {
                    val personName = acct.displayName
                    val personGivenName = acct.givenName
                    val personFamilyName = acct.familyName
                    val personEmail = acct.email
                    val personId = acct.id
                    val personPhoto: Uri? = acct.photoUrl
                    Toast.makeText(this,"Welcome $personName",Toast.LENGTH_SHORT).show()
                    if(GoogleSignIn.getLastSignedInAccount(this)!=null){
                        val user = User(personId)
                        Log.d(TAG,firebaseAuth.currentUser.toString())
                        user.email=personEmail.toString()
                        user.name = personName.toString()
                        Log.d(TAG,user.email + user.id + user.name)
                        val intent = Intent()
                        intent.putExtra("GoogleId",user.id)
//                        databaseReference.child("Users").child(user.id.toString()).setValue("")
//                        databaseReference.child("Users").child(user.id.toString()).child("Notes").setValue("")
//                        databaseReference.child("Users").child(user.id.toString()).child("Tasks").setValue("")
//                        databaseReference.child("Users").child(user.id.toString()).child("Reminders").setValue("")
                    }
                }
                finish()
            }
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e:ApiException){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

//    private fun displaySignIn() {
//        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this) { result ->
//            try {
//                val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                oneTapResult.launch(ib)
//            } catch (e:IntentSender.SendIntentException) {
//                Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
//            }
//        }.addOnFailureListener(this) { e ->
//            // No Google Accounts found. Just continue presenting the signed-out UI.
//            displaySignUp()
//            Log.d("btn click", e.localizedMessage!!)
//        }
//    }

//    private fun displaySignUp() {
//        oneTapClient.beginSignIn(signUpRequest)
//            .addOnSuccessListener(this) { result ->
//                try {
//                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                    oneTapResult.launch(ib)
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
//                }
//            }
//            .addOnFailureListener(this) { e ->
//                // No Google Accounts found. Just continue presenting the signed-out UI.
//                displaySignUp()
//                Log.d("btn click", e.localizedMessage!!)
//            }
//    }
}