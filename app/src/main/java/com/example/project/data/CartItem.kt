package com.example.project.data

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val cartItemId: Long,
    val cartProductId: String,
    val cartProductName: String,
    val cartImage: String,
    var cartQuantity: Int,
    val cartPrice: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readInt()?: 0,
        parcel.readDouble()?: 0.0
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(cartItemId)
        parcel.writeString(cartProductId)
        parcel.writeString(cartProductName)
        parcel.writeString(cartImage)
        parcel.writeInt(cartQuantity)
        parcel.writeDouble(cartPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}