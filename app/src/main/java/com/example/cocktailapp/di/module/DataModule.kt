package com.example.cocktailapp.di.module

import android.content.Context
import com.example.cocktailapp.BASE_URL
import com.example.cocktailapp.data.repository.CocktailRepository
import com.example.cocktailapp.data.repository.CocktailRepositoryImpl
import com.example.cocktailapp.data.repository.database.CocktailDatabase
import com.example.cocktailapp.data.repository.network.CocktailApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataModule(private val context: Context) {
    @Provides
    fun provideCocktailApiService(): CocktailApiService {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                ).build()
            )
            .build()
            .create(CocktailApiService::class.java)
    }

    @Provides
    fun provideDataBase(): CocktailDatabase {
        return CocktailDatabase.getInstance(context)
    }

    @Provides
    fun provideCocktailRepository(
        database: CocktailDatabase,
        apiService: CocktailApiService
    ): CocktailRepository {
        return CocktailRepositoryImpl(apiService, database.searchDao())
    }

}