package com.example.project.data

data class Order(
    val OrderId: String,
    val orderStatus: String,
    val total: Double,
    val orderAmount: Double,
    val items: Int,
    val date: String
)