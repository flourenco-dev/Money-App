package com.example.minimoney.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.minimoney.model.Account

@Entity
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String? = null,
    val totalPlanValue: Double,
    val accounts: List<Account>
)