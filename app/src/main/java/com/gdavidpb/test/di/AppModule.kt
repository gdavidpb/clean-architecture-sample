package com.gdavidpb.test.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.gdavidpb.test.data.source.local.LocalCacheDataStore
import com.gdavidpb.test.data.source.local.MusicCacheDataStore
import com.gdavidpb.test.data.source.local.MusicDatabase
import com.gdavidpb.test.data.source.remote.MusicRemoteDataStore
import com.gdavidpb.test.data.source.remote.iTunesSearchApi
import com.gdavidpb.test.domain.repository.MusicLocalRepository
import com.gdavidpb.test.domain.repository.MusicRemoteRepository
import com.gdavidpb.test.domain.repository.StorageRepository
import com.gdavidpb.test.domain.usecase.*
import com.gdavidpb.test.presentation.viewmodel.ArtistDetailViewModel
import com.gdavidpb.test.presentation.viewmodel.ArtistViewModel
import com.gdavidpb.test.presentation.viewmodel.FavoritesViewModel
import com.gdavidpb.test.presentation.viewmodel.SearchViewModel
import com.gdavidpb.test.utils.DATABASE_NAME
import com.gdavidpb.test.utils.URL_BASE_ITUNES_SEARCH_API
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module
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
    viewModel<ArtistDetailViewModel>()

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