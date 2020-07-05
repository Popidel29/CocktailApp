package com.example.cocktailapp.data.model

import com.google.gson.annotations.SerializedName

data class SearchModel(
    @SerializedName("drinks")
    val drinks: List<Drink>
)