package com.example.minimoney.ui.user

import androidx.recyclerview.widget.DiffUtil
import com.example.minimoney.model.Account

class AccountDiffUtilCallback: DiffUtil.ItemCallback<Account>() {

    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
        oldItem == newItem
}