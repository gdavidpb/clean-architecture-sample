package cl.yapo.test.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import cl.yapo.test.R
import cl.yapo.test.presentation.viewmodel.MainViewModel
import cl.yapo.test.ui.fragments.FavoritesFragment
import cl.yapo.test.ui.fragments.SearchFragment
import cl.yapo.test.utils.notNull
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(this)

        /* Anchor view model to this activity lifecycle */
        viewModel
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        loadFragment(itemId = item.itemId)

        return true
    }

    private fun loadFragment(@IdRes itemId: Int) {
        when (itemId) {
            R.id.nav_search -> R.string.nav_search to SearchFragment()
            R.id.nav_favorites -> R.string.nav_favorites to FavoritesFragment()
            else -> null
        }.notNull { (title, fragment) ->
            supportActionBar?.setTitle(title)

            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_content, fragment)
                .commit()
        }
    }
}
