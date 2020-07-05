package com.example.cocktailapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cocktailapp.data.repository.CocktailRepositoryImpl
import com.example.cocktailapp.ui.home.search.SearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException

class SearchViewModelTest {
    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var cocktailRepository: CocktailRepositoryImpl

    lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SearchViewModel(cocktailRepository)
    }

    @Test
    fun `when cocktail repository returns search data successful the state should be fetch data success`() {
        //when
        every { cocktailRepository.getSearchResults("a") } returns Observable.just(emptyList())
        //then
        viewModel.getSearchResults("a")
        //verify
        assert( viewModel.state.value is SearchViewModel.ViewState.FetchDataSuccess)
    }


    @Test
    fun `when cocktail repository returns error the state should be fetch data error`() {
        //when
        every { cocktailRepository.getSearchResults("a") } returns Observable.error(RuntimeException("demo"))
        //then
        viewModel.getSearchResults("a")
        //verify
        assert( viewModel.state.value is SearchViewModel.ViewState.FetchDataError)
        assert( (viewModel.state.value as SearchViewModel.ViewState.FetchDataError).message == "demo")
    }


    @Test
    fun `when cocktail repository returns suggestion data successful the state should be fetch suggession success`() {
        //when
        every { cocktailRepository.getSuggestions("a") } returns Single.just(emptyList())
        //then
        viewModel.getSuggestions("a")
        //verify
        assert( viewModel.state.value is SearchViewModel.ViewState.FetchSuggestionSuccess)
    }


    @Test
    fun `when cocktail repository returns error for suggestion the state should be fetch suggestion error`() {
        //when
        every { cocktailRepository.getSuggestions("a") } returns Single.error(RuntimeException("demo"))
        //then
        viewModel.getSuggestions("a")
        //verify
        assert( viewModel.state.value is SearchViewModel.ViewState.FetchSuggestionError)
        assert( (viewModel.state.value as SearchViewModel.ViewState.FetchSuggestionError).message == "demo")
    }

}