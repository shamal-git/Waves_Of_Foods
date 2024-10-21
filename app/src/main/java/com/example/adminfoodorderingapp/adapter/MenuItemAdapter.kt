package com.example.adminfoodorderingapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodorderingapp.AllItemActivity
import com.example.adminfoodorderingapp.databinding.ItemItemBinding
import com.example.adminfoodorderingapp.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: AllItemActivity,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,

    private val onDeleteClickListener: (position : Int) -> Unit

) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    // Array to keep track of item quantities
    private val itemQuantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)

                foodNameTextView.text = menuItem.foodName
                pricetextView.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodImageView)

                quantitytextView.text = itemQuantities[position].toString()

                // Handle minus button click
                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }

                // Handle plus button click
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }

            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.quantitytextView.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.quantitytextView.text = itemQuantities[position].toString()
            } else {
                deleteItem(position)
            }
        }

        private fun deleteItem(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }
    }
}
