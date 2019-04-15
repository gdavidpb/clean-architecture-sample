package cl.yapo.test.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.yapo.test.R
import cl.yapo.test.utils.EXTRA_TITLE
import cl.yapo.test.utils.EXTRA_URL
import kotlinx.android.synthetic.main.activity_web_view.*
import java.net.URI

class WebViewActivity : AppCompatActivity() {

    private val extraUrl by lazy {
        intent.getStringExtra(EXTRA_URL)
    }

    private val extraTitle by lazy {
        intent.getStringExtra(EXTRA_TITLE)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        supportActionBar?.title = extraTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (isAppleHost(url = extraUrl)) {

            with(webView) {
                settings.javaScriptEnabled = true

                webChromeClient = android.webkit.WebChromeClient()

                webView.loadUrl(extraUrl)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun isAppleHost(url: String): Boolean {
        return URI(url).host.endsWith("apple.com")
    }
}
