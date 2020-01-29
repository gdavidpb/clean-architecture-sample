package com.gdavidpb.test.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.gdavidpb.test.R
import com.gdavidpb.test.ui.fragments.FavoritesFragment
import com.gdavidpb.test.ui.fragments.SearchFragment
import com.gdavidpb.test.utils.extensions.notNull
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val searchFragment by lazy {
        SearchFragment()
    }

    private val favoritesFragment by lazy {
        FavoritesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(R.id.nav_search)

        navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        loadFragment(itemId = item.itemId)

        return true
    }

    private fun loadFragment(@IdRes itemId: Int) {
        when (itemId) {
            R.id.nav_search -> searchFragment
            R.id.nav_favorites -> favoritesFragment
            else -> null
        }.notNull { fragment ->
            supportFragmentManager
                .beginTransaction()
                //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_content, fragment)
                .commit()
        }
    }
}
