package com.example.cocktailapp.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cocktailapp.data.model.Drink
import com.example.cocktailapp.data.repository.CocktailRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val cocktailRepository: CocktailRepository) :
    ViewModel() {
    private val disposable = io.reactivex.disposables.CompositeDisposable()
    private val _state = MutableLiveData<ViewState>()
    val state: LiveData<ViewState>
        get() = _state

    fun getSearchResults(query: String) {
        _state.postValue(ViewState.Loading)
        disposable.add(
            cocktailRepository.getSearchResults(query)
                .subscribe({ data ->
                    _state.postValue(ViewState.FetchDataSuccess(data))
                }, { error ->
                    _state.postValue(ViewState.FetchDataError(error.localizedMessage ?: ""))
                })
        )
    }

    fun getSuggestions(query: String = "") {
        disposable.add(
            cocktailRepository.getSuggestions(query)
                .subscribe({ data ->
                    _state.postValue(ViewState.FetchSuggestionSuccess(data))
                }, { error ->
                    _state.postValue(ViewState.FetchSuggestionError(error.localizedMessage ?: ""))
                })
        )
    }


    override fun onCleared() {
        disposable.clear()
    }


    sealed class ViewState {
        object Loading : ViewState()
        data class FetchDataError(val message: String) : ViewState()
        data class FetchDataSuccess(val data: List<Drink>) : ViewState()
        data class FetchSuggestionSuccess(val data: List<String>) : ViewState()
        data class FetchSuggestionError(val message: String) : ViewState()

    }
}