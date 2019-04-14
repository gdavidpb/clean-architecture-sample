package cl.yapo.test.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cl.yapo.test.R
import cl.yapo.test.utils.EXTRA_ARTIST_ID
import cl.yapo.test.utils.EXTRA_ARTIST_NAME
import kotlinx.android.synthetic.main.activity_artist.*

class ArtistActivity : AppCompatActivity() {

    private val extraArtistId by lazy {
        intent.getStringExtra(EXTRA_ARTIST_ID)
    }

    private val extraArtistName by lazy {
        intent.getStringExtra(EXTRA_ARTIST_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        supportActionBar?.title = extraArtistName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rViewTracks.layoutManager = LinearLayoutManager(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}
