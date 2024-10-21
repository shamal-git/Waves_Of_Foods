package com.example.adminfoodorderingapp

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodorderingapp.databinding.ActivitySignUpBinding
import com.example.adminfoodorderingapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class signUpActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var restaurantName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Firebase initialize
        auth = Firebase.auth
        //Firebase database
        database = Firebase.database.reference

        binding.createUserButton.setOnClickListener {
            //get text from edittext
            userName = binding.name.text.toString().trim()
            restaurantName = binding.restaurantName.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (userName.isBlank() || restaurantName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }

        }
        binding.alreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val locationList = arrayOf("Colombo", "Gampaha", "Miriswaththa", "Negombo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed!", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)

            }
        }
    }
//save data into database
    private fun saveUserData() {
        userName = binding.name.text.toString().trim()
        restaurantName = binding.restaurantName.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
//save user data to firebase database
        val user = UserModel(userName,restaurantName,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}