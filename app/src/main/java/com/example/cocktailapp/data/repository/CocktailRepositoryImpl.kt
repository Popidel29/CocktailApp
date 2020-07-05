package com.example.cocktailapp.data.repository

import com.example.cocktailapp.data.model.Drink
import com.example.cocktailapp.data.repository.database.DrinkDao
import com.example.cocktailapp.data.repository.network.CocktailApiService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val client: CocktailApiService,
    private val drinkDao: DrinkDao
) : CocktailRepository {
    override fun getSearchResults(query: String): Observable<List<Drink>> {

        return Observable.create(ObservableOnSubscribe<List<Drink>> { emitter ->
            var offlineResult = drinkDao.getDrinks(query)
            emitter.onNext(offlineResult)
            try {
                val response = client.getSearchResults(query).execute()
                if (response.isSuccessful) {
                    response.body().let { result ->
                        drinkDao.addDrinks(
                            result?.drinks ?: emptyList()
                        )
                        offlineResult =
                            drinkDao.getDrinks(query)
                        emitter.onNext(offlineResult)
                    }
                } else {
                    if (offlineResult.isEmpty()) {
                        emitter.onError(RuntimeException("Error code " + response.code()))
                    }
                }
            } catch (e: Exception) {
                if (offlineResult.isEmpty()) {
                    emitter.onError(e)
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

    override fun getSuggestions(query: String): Single<List<String>> {
        return drinkDao
            .getSuggestions(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}