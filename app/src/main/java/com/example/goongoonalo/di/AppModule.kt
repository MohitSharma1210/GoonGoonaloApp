package com.example.goongoonalo.di

import android.content.Context
import com.example.goongoonalo.data.network.ApiService
import com.example.goongoonalo.utils.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbxt1XRoFRYm9-g0Tz9Ise9nZwxdUAUTYy6kLh-ISFaG2rWrFbUE0LUDOISWBjXCFHI2Ug/"

    @Provides
    @Singleton
    fun provideApiService(): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context
    ): PreferenceManager = PreferenceManager(context)
}

