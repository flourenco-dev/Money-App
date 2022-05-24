package com.example.minimoney.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimoney.core.Repository
import com.example.minimoney.model.Account
import com.example.minimoney.model.FirstScreenState
import com.example.minimoney.model.LoginFailedException
import com.example.minimoney.model.LoginRequestState
import com.example.minimoney.model.RequestFailedException
import com.example.minimoney.model.UnauthorizedException
import com.example.minimoney.model.User
import com.example.minimoney.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val repository: Repository): ViewModel() {

    companion object {
        const val valueToAdd = 10.0
        const val invalidAccountId = -1
    }

    private val showLoadingSingleLiveEvent = SingleLiveEvent<Any>()
    val showLoadingObservable: LiveData<Any> = showLoadingSingleLiveEvent

    private val hideLoadingSingleLiveEvent = SingleLiveEvent<Any>()
    val hideLoadingObservable: LiveData<Any> = hideLoadingSingleLiveEvent

    private val unauthorizedEventSingleLiveEvent = SingleLiveEvent<Any>()
    val unauthorizedEventObservable: LiveData<Any> = unauthorizedEventSingleLiveEvent

    private val goToLoginSingleLiveEvent = SingleLiveEvent<Any>()
    val goToLoginObservable: LiveData<Any> = goToLoginSingleLiveEvent

    private val goToUserAccountSingleLiveEvent = SingleLiveEvent<Any>()
    val goToUserAccountObservable: LiveData<Any> = goToUserAccountSingleLiveEvent

    private val loginRequestSingleLiveEvent = SingleLiveEvent<LoginRequestState>()
    val loginRequestStateObservable: LiveData<LoginRequestState> = loginRequestSingleLiveEvent

    fun decideFirstScreen() {
        when (repository.getFirstScreenState()) {
            FirstScreenState.Login -> goToLoginSingleLiveEvent.call()
            FirstScreenState.UserAccounts -> goToUserAccountSingleLiveEvent.call()
        }
    }

    fun doLogin(email: String, password: String, name: String?) {
        viewModelScope.launch {
            try {
                showLoadingSingleLiveEvent.call()
                repository.performLogin(email, password, name)
                loginRequestSingleLiveEvent.postValue(LoginRequestState.Success)
            } catch (error: UnauthorizedException) {
                unauthorizedEventSingleLiveEvent.call()
            } catch (error: LoginFailedException) {
                loginRequestSingleLiveEvent.postValue(error.loginRequestState)
            } catch (error: Exception) {
                Timber.e(error)
                loginRequestSingleLiveEvent.postValue(LoginRequestState.GenericError)
            } finally {
                hideLoadingSingleLiveEvent.call()
            }
        }
    }

    fun getUserObservable(): LiveData<User?> = repository.getUserObservable()

    fun triggerGetUser() {
        viewModelScope.launch {
            try {
                showLoadingSingleLiveEvent.call()
                repository.triggerGetUser()
            } catch (error: UnauthorizedException) {
                unauthorizedEventSingleLiveEvent.call()
            } catch (error: Exception) {
                Timber.e(error)
            } finally {
                hideLoadingSingleLiveEvent.call()
            }
        }
    }

    fun getAccountByIdObservable(accountId: Int): LiveData<Account?> =
        repository.getAccountByIdObservable(accountId)

    fun addToAccountById(accountId: Int): LiveData<Boolean> {
        val addResult = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                showLoadingSingleLiveEvent.call()
                repository.makePaymentToAccount(
                    accountId = accountId,
                    value = valueToAdd
                )
                addResult.postValue(true)
            } catch (error: UnauthorizedException) {
                unauthorizedEventSingleLiveEvent.call()
            } catch (error: RequestFailedException) {
                Timber.e(error)
                addResult.postValue(false)
            } finally {
                hideLoadingSingleLiveEvent.call()
            }
        }
        return addResult
    }

    fun doLogout(): LiveData<Any> {
        val logoutResult = SingleLiveEvent<Any>()
        viewModelScope.launch {
            try {
                showLoadingSingleLiveEvent.call()
                repository.performLogout()
            } catch (error: Exception) {
                Timber.e(error)
            } finally {
                hideLoadingSingleLiveEvent.call()
                logoutResult.call()
            }
        }
        return logoutResult
    }
}