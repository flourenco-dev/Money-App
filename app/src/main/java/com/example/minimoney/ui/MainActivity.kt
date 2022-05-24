package com.example.minimoney.ui

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.minimoney.R
import com.example.minimoney.databinding.ActivityMainBinding
import com.example.minimoney.utils.accountIdExtra
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity(), AppNavigation {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private var navController: NavController? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = mainNavHostFragment.navController
        setupObservers()
    }

    private fun setupObservers() {
        mainViewModel.showLoadingObservable.observe(this) {
            showLoading()
        }
        mainViewModel.hideLoadingObservable.observe(this) {
            hideLoading()
        }
        mainViewModel.unauthorizedEventObservable.observe(this) {
            showUnauthorizedDialog()
        }
    }

    private fun showLoading() {
        loadingDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
        }
        loadingDialog?.show()
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private fun showUnauthorizedDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.unauthorized_dialog_title))
            .setMessage(getString(R.string.unauthorized_dialog_message))
            .setPositiveButton(getString(R.string.button_ok_label)) { dialog, _ ->
                dialog.dismiss()
                mainViewModel.doLogout().observe(this) {
                    navController?.navigate(R.id.navigate_to_decision)
                }
            }
            .setCancelable(false)
            .show()
    }

    override fun goToLoginFromDecision() {
        navController?.navigate(R.id.navigate_from_decision_to_login)
    }

    override fun goToUserAccountsFromDecision() {
        navController?.navigate(R.id.navigate_from_decision_to_user_accounts)
    }

    override fun goToUserAccountsFromLogin() {
        navController?.navigate(R.id.navigate_from_login_to_user_accounts)
    }

    override fun goToIndividualAccountFromUserAccounts(accountId: Int) {
        val extras = Bundle().also {
            it.putInt(accountIdExtra, accountId)
        }
        navController?.navigate(R.id.navigate_from_user_accounts_to_individual_account, extras)
    }
}