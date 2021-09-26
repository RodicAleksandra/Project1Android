package com.example.project.data

import android.os.Parcel
import android.os.Parcelable

data class Category (
    val idCategory: String,
    val strCategoryThumb: String,
    val strCategoryName:String,
    val strCategoryDesc: String)
    : Parcelable

{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idCategory)
        parcel.writeString(strCategoryName)
        parcel.writeString(strCategoryThumb)
        parcel.writeString(strCategoryDesc)
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }


}