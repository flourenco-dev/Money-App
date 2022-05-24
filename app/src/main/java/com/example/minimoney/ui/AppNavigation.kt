package com.example.minimoney.ui

interface AppNavigation {
    fun goToLoginFromDecision()
    fun goToUserAccountsFromDecision()
    fun goToUserAccountsFromLogin()
    fun goToIndividualAccountFromUserAccounts(accountId: Int)
}