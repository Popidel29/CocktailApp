package com.example.cocktailapp.data.repository.network

import com.example.cocktailapp.SEARCH_ENDPOINT
import com.example.cocktailapp.data.model.SearchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.Callable

interface CocktailApiService {
    @GET(SEARCH_ENDPOINT)
    fun getSearchResults(@Query("s") query: String): Call<SearchModel>
}