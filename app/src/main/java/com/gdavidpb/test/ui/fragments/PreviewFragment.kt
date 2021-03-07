package com.gdavidpb.test.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import androidx.navigation.fragment.navArgs
import com.gdavidpb.test.R
import kotlinx.android.synthetic.main.fragment_preview.*
import java.net.URI

class PreviewFragment : NavigationFragment() {

    private val args by navArgs<PreviewFragmentArgs>()

    override fun onCreateView() = R.layout.fragment_preview

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAppleHost(url = args.url)) {
            with(webView) {
                settings.javaScriptEnabled = true

                webChromeClient = WebChromeClient()

                webView.loadUrl(args.url)
            }
        }
    }

    private fun isAppleHost(url: String): Boolean {
        return URI(url).host.endsWith("apple.com")
    }
}
