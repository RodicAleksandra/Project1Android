package com.example.project.data

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val productId:String,
    val productName:String,
    val description:String,
    val price:Double,
    val quantity:Int,
    val image:String,
    val mrp:Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
        parcel.writeString(image)
        parcel.writeInt(mrp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}