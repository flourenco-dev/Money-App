package com.example.minimoney.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.minimoney.ui.AppNavigation

abstract class BaseFragment : Fragment() {

    protected var navigation: AppNavigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation = activity as? AppNavigation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseViewBinding()
    }

    abstract fun setupUi()
    abstract fun setupListeners()
    abstract fun setupObservers()
    abstract fun releaseViewBinding()
}