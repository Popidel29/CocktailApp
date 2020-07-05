package com.example.cocktailapp.data.repository.database

import androidx.room.*
import com.example.cocktailapp.data.model.Drink
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DrinkDao {

    @Query("SELECT strDrink FROM drink WHERE strDrink LIKE '%' || :query  || '%' LIMIT 5")
    fun getSuggestions(query: String): Single<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addDrinks(result: List<Drink>): List<Long>

    @Query("SELECT * FROM drink WHERE strDrink LIKE '%' || :query  || '%'")
    fun getDrinks(query: String): List<Drink>


    @Query("SELECT * FROM drink WHERE isFavorite = 1")
    fun getFavourites(): Single<List<Drink>>


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDrink(drink: Drink): Completable
}