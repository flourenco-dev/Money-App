package com.example.minimoney.ui.decision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.minimoney.R
import com.example.minimoney.ui.AppNavigation
import com.example.minimoney.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DecisionFragment : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()
    private var navigation: AppNavigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation = activity as? AppNavigation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupObservers()
        viewModel.decideFirstScreen()
        return inflater.inflate(R.layout.fragment_decision, container, false)
    }

    private fun setupObservers() {
        viewModel.goToLoginObservable.observe(viewLifecycleOwner) {
            navigation?.goToLoginFromDecision()
        }
        viewModel.goToUserAccountObservable.observe(viewLifecycleOwner) {
            navigation?.goToUserAccountsFromDecision()
        }
    }
}