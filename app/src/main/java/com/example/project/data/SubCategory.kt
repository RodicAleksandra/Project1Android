package com.example.project.data

import android.os.Parcel
import android.os.Parcelable

data class SubCategory(
    val subCatImage:String,
    val subCatDescription:String,
    val subCatId:Int,
    val subCatName:String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readInt(),
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subCatImage)
        parcel.writeString(subCatDescription)
        parcel.writeInt(subCatId)
        parcel.writeString(subCatName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubCategory> {
        override fun createFromParcel(parcel: Parcel): SubCategory {
            return SubCategory(parcel)
        }

        override fun newArray(size: Int): Array<SubCategory?> {
            return arrayOfNulls(size)
        }
    }
}