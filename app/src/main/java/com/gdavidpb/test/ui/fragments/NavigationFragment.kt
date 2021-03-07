package com.gdavidpb.test.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class NavigationFragment : Fragment() {
    @LayoutRes
    abstract fun onCreateView(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(onCreateView(), container, false)
    }

    protected fun navigate(directions: NavDirections) = findNavController().navigate(directions)
}