package com.example.cocktailapp

import com.example.cocktailapp.data.model.Drink
import com.example.cocktailapp.data.model.SearchModel
import com.example.cocktailapp.data.repository.CocktailRepositoryImpl
import com.example.cocktailapp.data.repository.database.DrinkDao
import com.example.cocktailapp.data.repository.network.CocktailApiService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class CocktailRepositoryTest {

    @JvmField
    @Rule
    val rule = RxImmediateSchedulerRule()

    @MockK
    lateinit var apiService: CocktailApiService

    @MockK
    lateinit var drinkDao: DrinkDao

    lateinit var cocktailRepository: CocktailRepositoryImpl

    @MockK
    lateinit var mockCall: Call<SearchModel>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        cocktailRepository = CocktailRepositoryImpl(apiService, drinkDao)
    }

    @Test
    fun `when getSuggestion with a is called it should call drinkDao's get suggestion with a`() {
        //when
        every { drinkDao.getSuggestions("a") } returns Single.just(emptyList())
        //then
        cocktailRepository.getSuggestions("a")
        //verify
        verify { drinkDao.getSuggestions("a") }

    }

    @Test
    fun `when getSearchResults is called it should call drinkDao's getDrinks and then Api call and again drinkDao's getDrinks`() {
        //when
        val dummyDrink = Drink("demo")
        every { drinkDao.getDrinks("a") } returns emptyList()
        every { drinkDao.addDrinks(listOf(dummyDrink)) } returns emptyList()
        every { apiService.getSearchResults("a") } returns mockCall
        every { mockCall.execute() } returns Response.success(SearchModel(listOf(dummyDrink)))
        //then
        cocktailRepository.getSearchResults("a").subscribe()
        //verify
        verify(Ordering.ORDERED) {
            drinkDao.getDrinks("a")
            apiService.getSearchResults("a")
            drinkDao.addDrinks(listOf(dummyDrink))
            drinkDao.getDrinks("a")
        }

    }


    @Test
    fun `when getSearchResults is called and API call gave exception then it should call drinkDao's getDrinks and then Api call and onError`() {
        //when
        val dummyDrink = Drink("demo")
        val exception = RuntimeException("demo")
        every { drinkDao.getDrinks("a") } returns emptyList()
        every { drinkDao.addDrinks(listOf(dummyDrink)) } returns emptyList()
        every { apiService.getSearchResults("a") } returns mockCall
        every { mockCall.execute() } throws exception
        //then
        val testSubscriber = TestObserver<List<Drink>>()
        cocktailRepository.getSearchResults("a").subscribe(testSubscriber)
        //verify
        testSubscriber.assertError(exception)
        verify(Ordering.ORDERED) {
            drinkDao.getDrinks("a")
            apiService.getSearchResults("a")
        }

    }


    @Test
    fun `when getSearchResults is called it gave fail response then should call drinkDao's getDrinks and then Api call `() {
        //when
        val dummyDrink = Drink("demo")
        val exception = RuntimeException("Error code 500")
        val mockResponseBody: ResponseBody = mockk()
        every { drinkDao.getDrinks("a") } returns emptyList()
        every { drinkDao.addDrinks(listOf(dummyDrink)) } returns emptyList()
        every { apiService.getSearchResults("a") } returns mockCall
        every { mockResponseBody.contentType() } returns "application/json".toMediaType()
        every { mockResponseBody.contentLength() } returns 10
        every { mockCall.execute() } returns Response.error(500, mockResponseBody)
        //then
        val testSubscriber = TestObserver<List<Drink>>()
        cocktailRepository.getSearchResults("a").subscribe(testSubscriber)
        //verify
        verify(Ordering.ORDERED) {
            drinkDao.getDrinks("a")
            apiService.getSearchResults("a")
        }

    }


}