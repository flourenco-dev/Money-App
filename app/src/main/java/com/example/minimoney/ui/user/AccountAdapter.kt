package com.example.minimoney.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoney.R
import com.example.minimoney.databinding.ItemAccountBinding
import com.example.minimoney.model.Account
import com.example.minimoney.utils.getCompatColor
import com.example.minimoney.utils.isColorHex
import com.example.minimoney.utils.setOnClickListenerWithHaptic

class AccountAdapter(private val listener: Listener):
    ListAdapter<Account, AccountAdapter.ViewHolder>(AccountDiffUtilCallback()) {

    fun updateAccounts(newAccounts: List<Account>) {
        submitList(newAccounts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemAccountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemAccountBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindView(account: Account) {
            binding.accountContainer.setCardBackgroundColor(
                if (account.colorCode.isColorHex()) {
                    account.colorCode.toColorInt()
                } else {
                    binding.root.context.getCompatColor(R.color.white)
                }
            )
            binding.accountNameText.text = account.name
            binding.accountPlanValueText.text =
                binding.root.context.getString(
                    R.string.account_plan_value_text,
                    account.planValue
                )
            binding.accountMoneyboxText.text =
                binding.root.context.getString(
                    R.string.account_moneybox_text,
                    account.moneybox
                )

            binding.accountContainer.setOnClickListenerWithHaptic {
                listener.onAccountClicked(account)
            }
        }
    }

    interface Listener {
        fun onAccountClicked(account: Account)
    }
}