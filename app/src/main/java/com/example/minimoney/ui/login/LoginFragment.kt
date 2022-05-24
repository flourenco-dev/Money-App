package com.example.minimoney.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.minimoney.R
import com.example.minimoney.databinding.FragmentLoginBinding
import com.example.minimoney.model.LoginRequestState
import com.example.minimoney.ui.MainViewModel
import com.example.minimoney.ui.base.BaseFragment
import com.example.minimoney.utils.getStringText
import com.example.minimoney.utils.isValidEmail
import com.example.minimoney.utils.setOnClickListenerWithHaptic
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment: BaseFragment() {

    private var binding: FragmentLoginBinding? = null
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun setupUi() {
        binding?.loginSignInButton?.isEnabled = false
    }

    override fun setupListeners() {
        binding?.loginEmailInput?.addTextChangedListener {
            validateInputsAndChangeSignButtonState()
        }
        binding?.loginPasswordInput?.addTextChangedListener {
            validateInputsAndChangeSignButtonState()
        }
        binding?.loginSignInButton?.setOnClickListenerWithHaptic {
            binding?.loginAnimationView?.playAnimation()
            val email = binding?.loginEmailInput?.text.getStringText()
            val password = binding?.loginPasswordInput?.text.getStringText()
            val name = binding?.loginNameInput?.text?.toString()
            viewModel.doLogin(email, password, name)
        }
    }

    private fun validateInputsAndChangeSignButtonState() {
        binding?.loginEmailInputContainer?.isErrorEnabled = false
        binding?.loginPasswordInputContainer?.isErrorEnabled = false
        binding?.apply {
            loginSignInButton.isEnabled = loginEmailInput.text.isValidEmail() &&
                    !loginPasswordInput.text.isNullOrEmpty()
        }
    }

    override fun setupObservers() {
        viewModel.loginRequestStateObservable.observe(viewLifecycleOwner) {
            when (it) {
                LoginRequestState.Success -> navigation?.goToUserAccountsFromLogin()
                LoginRequestState.WrongEmailOrPassword -> {
                    showLoginError(
                        errorText = getString(R.string.login_error_wrong_password_or_email)
                    )
                }
                LoginRequestState.TooManyAttempts -> {
                    showLoginError(
                        errorText = getString(R.string.login_error_too_many_attempts)
                    )
                }
                else -> {
                    showLoginError(
                        errorText = getString(R.string.login_error_generic_error)
                    )
                }
            }
        }
    }

    private fun showLoginError(errorText: String) {
        binding?.loginEmailInputContainer?.error = " "
        binding?.loginPasswordInputContainer?.error = errorText
    }

    override fun releaseViewBinding() {
        binding = null
    }
}