package com.example.minimoney.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minimoney.R
import com.example.minimoney.databinding.FragmentUserAccountsBinding
import com.example.minimoney.model.Account
import com.example.minimoney.ui.MainViewModel
import com.example.minimoney.ui.base.BaseFragment
import com.example.minimoney.utils.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserAccountsFragment: BaseFragment(), AccountAdapter.Listener {

    private var binding: FragmentUserAccountsBinding? = null
    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun setupUi() {
        accountAdapter = AccountAdapter(listener = this)
        binding?.userAccountsRecycler?.adapter = accountAdapter
    }

    override fun setupListeners() {
        binding?.userAccountsSwipeRefreshLayout?.setOnRefreshListener {
            viewModel.triggerGetUser()
        }
    }

    override fun setupObservers() {
        viewModel.getUserObservable().observe(viewLifecycleOwner) { user ->
            user?.let {
                if (binding?.userAccountsSwipeRefreshLayout?.isRefreshing.get()) {
                    binding?.userAccountsSwipeRefreshLayout?.isRefreshing = false
                }
                binding?.userAccountsNameText?.text = if (user.name.isNullOrEmpty()) {
                    getString(R.string.user_accounts_hello_without_name_text)
                } else {
                    getString(R.string.user_accounts_hello_with_name_text, user.name)
                }
                binding?.userAccountsTotalPlanValueText?.text =
                    getString(R.string.user_accounts_total_plan_value_text, user.totalPlanValue)
                accountAdapter.updateAccounts(user.accounts)
            }
        }
        viewModel.triggerGetUser()
    }

    override fun releaseViewBinding() {
        binding = null
    }

    override fun onAccountClicked(account: Account) {
        navigation?.goToIndividualAccountFromUserAccounts(accountId = account.id)
    }
}