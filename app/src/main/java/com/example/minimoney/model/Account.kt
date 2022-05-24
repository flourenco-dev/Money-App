package com.example.minimoney.model

data class Account(
    val id: Int,
    val name: String,
    val friendlyName: String,
    val planValue: Double,
    val moneybox: Double,
    val colorCode: String
)
