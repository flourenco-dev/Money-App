package com.example.minimoney.model

data class User(
    val id: String,
    val name: String? = null,
    val totalPlanValue: Double,
    val accounts: List<Account>
)
