package com.example.cocktailapp.data.repository

import com.example.cocktailapp.data.model.Drink
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface CocktailRepository {
    fun getSearchResults(query: String): Observable<List<Drink>>

    fun getSuggestions(query: String): Single<List<String>>

}