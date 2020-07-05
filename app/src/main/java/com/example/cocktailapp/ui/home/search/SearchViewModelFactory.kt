package com.example.cocktailapp.ui.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cocktailapp.data.repository.CocktailRepository
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(private val cocktailRepository: CocktailRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(
            cocktailRepository
        ) as T
    }
}