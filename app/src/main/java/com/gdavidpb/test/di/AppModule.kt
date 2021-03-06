package com.gdavidpb.test.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.gdavidpb.test.BuildConfig
import com.gdavidpb.test.data.model.database.DatabaseModel
import com.gdavidpb.test.data.source.iTunesDataRepository
import com.gdavidpb.test.data.source.iTunesDataStoreFactory
import com.gdavidpb.test.data.source.local.AndroidNetworkDataStore
import com.gdavidpb.test.data.source.local.LocalCacheDataStore
import com.gdavidpb.test.data.source.local.MusicDatabase
import com.gdavidpb.test.data.source.local.iTunesLocalDataStore
import com.gdavidpb.test.data.source.remote.iTunesRemoteDataStore
import com.gdavidpb.test.data.source.remote.iTunesSearchApi
import com.gdavidpb.test.domain.repository.MusicRepository
import com.gdavidpb.test.domain.repository.NetworkRepository
import com.gdavidpb.test.domain.repository.StorageRepository
import com.gdavidpb.test.domain.usecase.*
import com.gdavidpb.test.presentation.viewmodel.AlbumViewModel
import com.gdavidpb.test.presentation.viewmodel.ArtistViewModel
import com.gdavidpb.test.presentation.viewmodel.FavoritesViewModel
import com.gdavidpb.test.presentation.viewmodel.SearchViewModel
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
            .baseUrl(BuildConfig.API_BASE)
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
            DatabaseModel.NAME
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
    viewModel<AlbumViewModel>()

    /* Repositories */

    factoryBy<MusicRepository, iTunesDataRepository>()
    factoryBy<StorageRepository, LocalCacheDataStore>()
    factoryBy<NetworkRepository, AndroidNetworkDataStore>()

    /* Data stores */

    factory<iTunesLocalDataStore>()
    factory<iTunesRemoteDataStore>()

    /* Factory */

    factory<iTunesDataStoreFactory>()

    /* Use cases */

    factory<GetTrackPreviewUseCase>()
    factory<LikeArtistUseCase>()
    factory<UnlikeArtistUseCase>()
    factory<LookupAlbumsUseCase>()
    factory<LookupTracksUseCase>()
    factory<SearchArtistsUseCase>()
    factory<GetLikedArtistsUseCase>()
}