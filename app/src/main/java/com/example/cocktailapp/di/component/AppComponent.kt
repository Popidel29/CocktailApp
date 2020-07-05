package com.example.cocktailapp.di.component

import com.example.cocktailapp.MyApp
import com.example.cocktailapp.data.repository.CocktailRepository
import com.example.cocktailapp.data.repository.database.CocktailDatabase
import com.example.cocktailapp.data.repository.network.CocktailApiService
import com.example.cocktailapp.di.module.DataModule
import com.example.cocktailapp.ui.home.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [DataModule::class])
@Singleton
interface AppComponent {

    fun inject(application: MyApp)

    fun database(): CocktailDatabase

    fun networkService(): CocktailApiService

    fun repository(): CocktailRepository

    fun injectSearchFragment(fragment: SearchFragment)
}