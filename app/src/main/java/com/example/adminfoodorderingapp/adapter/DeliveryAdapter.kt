package com.example.adminfoodorderingapp.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodorderingapp.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customerNames: MutableList<String>,
    private val paymentStatus: MutableList<Boolean>
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerNameTextView.text = customerNames[position]
                if (paymentStatus[position] == true){
                    paymentStatusTextView.text = "Received"
                }
                else{
                    paymentStatusTextView.text = "Not Received"
                }

                val colorMap = mapOf(
                    true to Color.GREEN,
                    false to Color.RED,
                )
                paymentStatusTextView.setTextColor(colorMap[paymentStatus[position]] ?: Color.BLACK)
                statusColor.backgroundTintList =
                    ColorStateList.valueOf(colorMap[paymentStatus[position]] ?: Color.BLACK)
            }
        }

    }
}