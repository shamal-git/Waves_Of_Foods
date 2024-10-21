package com.example.adminfoodorderingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfoodorderingapp.databinding.ActivityLoginBinding
import com.example.adminfoodorderingapp.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class LoginActivity : AppCompatActivity() {
    private var userName: String ?= null
    private var restaurantName: String ?= null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions:GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
//initialize firebase auth
        auth = Firebase.auth
        //initialize firebase database
        database = Firebase.database.reference
        //google sign In
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        binding.loginButton.setOnClickListener {
            //get
            email = binding.emailEditText.text.toString().trim()
            password = binding.passwordEditText.text.toString().trim()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details.", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount(email, password)
            }

        }
        binding.googleButton.setOnClickListener {
            val signIntent:Intent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.donthaveAccountButton.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this,"Login Successfully!", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        Toast.makeText(this,"Create User & Login Successfully!", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserAccount: Authentication failed", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email = binding.emailEditText.text.toString().trim()
        password = binding.passwordEditText.text.toString().trim()

        val user = UserModel(userName, restaurantName, email, password)
        val userId:String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

//launcher for google
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential:AuthCredential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful){
                        //successfully sign in with Google
                        Toast.makeText(this,"Successfully Sign in with Google",Toast.LENGTH_SHORT).show()
                        updateUi(authTask.result?.user)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Google Sign in Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this,"Google Sign in Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //check if user already logged or not
    override fun onStart() {
        super.onStart()
        val currentUser:FirebaseUser? = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}