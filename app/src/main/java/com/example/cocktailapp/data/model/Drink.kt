package com.example.cocktailapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "drink")
data class Drink(
    @PrimaryKey
    @SerializedName("idDrink")
    val idDrink: String = "",
    @SerializedName("strDrink")
    val strDrink: String? = null,
    @SerializedName("strTags")
    val strTags: String? = null,
    @SerializedName("strCategory")
    val strCategory: String? = null,
    @SerializedName("strAlcoholic")
    val strAlcoholic: String?= null,
    @SerializedName("strGlass")
    val strGlass: String? = null,
    @SerializedName("strInstructions")
    val strInstructions: String? = null,
    @SerializedName("strDrinkThumb")
    val strDrinkThumb: String? = null,
    @SerializedName("strIngredient1")
    val strIngredient1: String? = null,
    @SerializedName("strIngredient2")
    val strIngredient2: String? = null,
    @SerializedName("strIngredient3")
    val strIngredient3: String? = null,
    @SerializedName("strIngredient4")
    val strIngredient4: String? = null,
    @SerializedName("strIngredient5")
    val strIngredient5: String? = null,
    @SerializedName("strIngredient6")
    val strIngredient6: String? = null,
    var isFavorite: Boolean = false
)