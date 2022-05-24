package com.example.minimoney.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.minimoney.R
import com.example.minimoney.databinding.FragmentIndividualAccountBinding
import com.example.minimoney.ui.MainViewModel
import com.example.minimoney.ui.MainViewModel.Companion.invalidAccountId
import com.example.minimoney.ui.MainViewModel.Companion.valueToAdd
import com.example.minimoney.ui.base.BaseFragment
import com.example.minimoney.utils.accountIdExtra
import com.example.minimoney.utils.get
import com.example.minimoney.utils.setOnClickListenerWithHaptic
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class IndividualAccountFragment: BaseFragment() {
    private var binding: FragmentIndividualAccountBinding? = null
    private val viewModel: MainViewModel by sharedViewModel()
    private var accountId: Int = invalidAccountId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountId = arguments?.getInt(accountIdExtra).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return if (accountId == invalidAccountId) {
            null
        } else {
            binding = FragmentIndividualAccountBinding.inflate(inflater, container, false)
            binding?.root
        }
    }

    override fun setupUi() {
        binding?.individualAccountAddButton?.text =
            getString(R.string.individual_account_add_button_text, valueToAdd)
    }

    override fun setupListeners() {
        binding?.individualAccountAddButton?.setOnClickListenerWithHaptic {
            binding?.individualAccountAnimationView?.playAnimation()
            viewModel.addToAccountById(accountId).observe(viewLifecycleOwner) { success ->
                    val message = getString(
                        if (success) {
                            R.string.individual_account_add_request_success
                        } else {
                            R.string.individual_account_add_request_fail
                        }
                    )
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun setupObservers() {
        viewModel.getAccountByIdObservable(accountId).observe(viewLifecycleOwner) { account ->
            account?.let {
                binding?.individualAccountNameText?.text = account.name
                binding?.individualAccountPlanValueText?.text = getString(
                    R.string.account_plan_value_text, account.planValue
                )
                binding?.individualAccountMoneyboxText?.text = getString(
                    R.string.account_moneybox_text, account.moneybox
                )
            }
        }
    }

    override fun releaseViewBinding() {
        binding = null
    }
}