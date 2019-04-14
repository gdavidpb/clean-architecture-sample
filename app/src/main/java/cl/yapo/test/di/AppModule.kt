package cl.yapo.test.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import cl.yapo.test.data.source.local.LocalCacheDataStore
import cl.yapo.test.data.source.local.MusicCacheDataStore
import cl.yapo.test.data.source.local.MusicDatabase
import cl.yapo.test.data.source.remote.MusicRemoteDataStore
import cl.yapo.test.data.source.remote.iTunesSearchApi
import cl.yapo.test.domain.repository.MusicLocalRepository
import cl.yapo.test.domain.repository.MusicRemoteRepository
import cl.yapo.test.domain.repository.StorageRepository
import cl.yapo.test.domain.usecase.*
import cl.yapo.test.presentation.viewmodel.ArtistViewModel
import cl.yapo.test.presentation.viewmodel.FavoritesViewModel
import cl.yapo.test.presentation.viewmodel.SearchViewModel
import cl.yapo.test.utils.DATABASE_NAME
import cl.yapo.test.utils.URL_BASE_ITUNES_SEARCH_API
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.factory
import org.koin.experimental.builder.factoryBy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    /* Android Services */

    single {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /* Retrofit */

    single {
        OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(URL_BASE_ITUNES_SEARCH_API)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>()
            .create(iTunesSearchApi::class.java) as iTunesSearchApi
    }

    /* Database */

    single {
        Room.databaseBuilder(
            androidContext(),
            MusicDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    /* Picasso */

    single {
        Picasso.get()
    }

    /* View models */
    viewModel<SearchViewModel>()
    viewModel<FavoritesViewModel>()
    viewModel<ArtistViewModel>()

    /* Factories */

    factoryBy<MusicLocalRepository, MusicCacheDataStore>()
    factoryBy<MusicRemoteRepository, MusicRemoteDataStore>()
    factoryBy<StorageRepository, LocalCacheDataStore>()

    /* Use cases */

    factory<DownloadTrackUseCase>()
    factory<LikeArtistUseCase>()
    factory<UnlikeArtistUseCase>()
    factory<LookupAlbumsUseCase>()
    factory<LookupTracksUseCase>()
    factory<SearchArtistsUseCase>()
    factory<GetLikedArtistsUseCase>()
}