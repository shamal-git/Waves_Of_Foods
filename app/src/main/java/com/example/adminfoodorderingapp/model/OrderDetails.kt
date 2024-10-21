package com.example.adminfoodorderingapp.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class OrderDetails() : Serializable {
    var userUid: String? = null
    var userName: String? = null
    var foodNames: MutableList<String>? = null
    var foodImages: MutableList<String>? = null
    var foodPrices: MutableList<String>? = null
    var foodQuantities: MutableList<Int>? = null
    var address: String? = null
    var TotalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String? = null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        TotalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    fun describeContents(): Int {
        return 0
    }

    fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(userUid)
        p0.writeString(userName)
        p0.writeString(address)
        p0.writeString(TotalPrice)
        p0.writeString(phoneNumber)
        p0.writeByte(if (orderAccepted) 1 else 0)
        p0.writeByte(if (paymentReceived) 1 else 0)
        p0.writeString(itemPushKey)
        p0.writeLong(currentTime)
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}