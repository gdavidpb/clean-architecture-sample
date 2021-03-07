package com.gdavidpb.test.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.gdavidpb.test.R
import com.gdavidpb.test.utils.extensions.SnackBarBuilder
import com.google.android.material.snackbar.Snackbar

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

    protected inline fun snackBar(
        length: Int = Snackbar.LENGTH_LONG,
        builder: SnackBarBuilder.() -> Unit
    ) = SnackBarBuilder(requireView(), length).apply(builder).build().show()

    protected fun noConnectionSnackBar(isNetworkAvailable: Boolean, retry: (() -> Unit)? = null) {
        snackBar {
            messageResource = if (isNetworkAvailable)
                R.string.snack_bar_connection_failure
            else
                R.string.snack_bar_no_connection

            if (retry != null) action(R.string.retry) { retry() }
        }
    }
}