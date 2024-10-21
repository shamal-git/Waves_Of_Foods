package com.example.adminfoodorderingapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderingapp.adapter.MenuItemAdapter
import com.example.adminfoodorderingapp.databinding.ActivityAllItemBinding
import com.example.adminfoodorderingapp.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllMenu> = ArrayList()

    private val binding : ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //
        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()



        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                //clear existing data
                menuItems.clear()

                //loop for each food item
                for (foodSnapshot in snapshot.children){
                    val Item = foodSnapshot.getValue(AllMenu::class.java)
                   Item?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error: ${error.message}")
            }

        })
    }
    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllItemActivity,menuItems,databaseReference){position ->
            deleteMenuItems(position)
        }
        binding.menuRecycleView.layoutManager = LinearLayoutManager(this)
        binding.menuRecycleView.adapter = adapter
    }

    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete = menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReference = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.menuRecycleView.adapter?.notifyItemRemoved(position)
            }
            else{
                Toast.makeText(this,"Item not Deleted.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}