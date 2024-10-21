package com.example.adminfoodorderingapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodorderingapp.adapter.OrderDetailsAdapter
import com.example.adminfoodorderingapp.databinding.ActivityOrderDetailsBinding
import com.example.adminfoodorderingapp.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding : ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName : String? = null
    private var address : String? = null
    private var phoneNumber : String? = null
    private var totalPrice : String? = null
    private var foodNames : ArrayList<String> = arrayListOf()
    private var foodImages : ArrayList<String> = arrayListOf()
    private var foodQuantity : ArrayList<Int> = arrayListOf()
    private var foodPrices : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let {orderDetails ->
                userName = receivedOrderDetails.userName
                foodNames = receivedOrderDetails.foodNames as ArrayList<String>
                foodImages = receivedOrderDetails.foodImages as ArrayList<String>
                foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>
                address = receivedOrderDetails.address
                phoneNumber = receivedOrderDetails.phoneNumber
                foodPrices = receivedOrderDetails.foodPrices as ArrayList<String>
                totalPrice = receivedOrderDetails.TotalPrice

                setUserDetails()
                setAdapter()
        }

    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames, foodImages,foodQuantity,foodPrices)
        binding.orderDetailsRecyclerView.adapter = adapter
    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address
        binding.name.text = userName
        binding.phone.text = phoneNumber
        binding.totalPay.text = totalPrice
    }
}