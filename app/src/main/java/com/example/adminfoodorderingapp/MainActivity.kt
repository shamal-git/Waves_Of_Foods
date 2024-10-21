package com.example.adminfoodorderingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodorderingapp.databinding.ActivityMainBinding
import com.example.adminfoodorderingapp.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.addMenu.setOnClickListener {
            val intent = Intent(this,AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this,AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.orderDispatch.setOnClickListener {
            val intent = Intent(this,outForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this,adminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createUser.setOnClickListener {
            val intent = Intent(this,CreateUserActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrderTextView.setOnClickListener {
            val intent = Intent(this,PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, signUpActivity::class.java))
            finish()
        }

        pendingOrders()
        completedOrders()
        wholeTimeEarning()

    }

    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()
        completedOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children){
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.TotalPrice?.replace("$", "")?.toIntOrNull()
                        ?.let { i ->
                            listOfTotalPay.add(i)
                        }
                }
                binding.earningTextView.text = listOfTotalPay.sum().toString() + "$"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun completedOrders() {
        val completeOrderReference = database.reference.child("CompletedOrder")
        var completeOrderItemCount = 0
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeOrderTextView.text = completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrdersTextView.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}