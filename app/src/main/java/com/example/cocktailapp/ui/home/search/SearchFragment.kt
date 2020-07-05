package com.example.cocktailapp.ui.home.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocktailapp.MyApp
import com.example.cocktailapp.R
import com.example.cocktailapp.data.model.Drink
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_item.view.*
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    lateinit var viewModel: SearchViewModel

    var currentQuery = ""
    var favourites = mutableSetOf<String>()
    private var currentItemSelected: View? = null

    lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.search_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_search.layoutManager = LinearLayoutManager(this.context)

        initializeSearchView()

        initializeDependencies()

        setupRecyclerView()

        setObservers()

        search_btn_retry.setOnClickListener {
            viewModel.getSearchResults(search_field.query.toString())
        }
        viewModel.getSearchResults("")

    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(mutableListOf()) { drink ->
            Toast.makeText(context, "It seems you like ${drink.strDrink}", Toast.LENGTH_LONG).show()
        }
        rv_search.layoutManager = LinearLayoutManager(context)
        rv_search.adapter = adapter
        rv_search.setOnScrollChangeListener { _, _, _, _, _ ->
            currentItemSelected?.also {
                deselectMenuItem(it)
                currentItemSelected = null
            }
        }
    }

    private fun setObservers() {
        //Network call call to retrieve search query and save to DB on success
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                SearchViewModel.ViewState.Loading -> {
                    displayProgressBar()
                }
                is SearchViewModel.ViewState.FetchDataError -> {
                    displayErrorMessage(state.message)
                }
                is SearchViewModel.ViewState.FetchDataSuccess -> {
                    displayData(state.data)
                }
            }
        })

    }

    private fun displayData(data: List<Drink>) {
        adapter.updateAllData(data)
        search_status_container.visibility = View.GONE
        search_pb_progress.visibility = View.GONE
        search_btn_retry.visibility = View.GONE
        search_tv_error.visibility = View.GONE
        rv_search.visibility = View.VISIBLE
        hideSoftKeyboard(this)
    }

    private fun displayErrorMessage(message: String) {
        search_status_container.visibility = View.VISIBLE
        search_pb_progress.visibility = View.GONE
        search_btn_retry.visibility = View.VISIBLE
        search_tv_error.visibility = View.VISIBLE
        rv_search.visibility = View.GONE
        search_tv_error.text = message
        hideSoftKeyboard(this)
    }

    private fun displayProgressBar() {
        search_status_container.visibility = View.VISIBLE
        search_tv_error.visibility = View.GONE
        search_pb_progress.visibility = View.VISIBLE
        search_btn_retry.visibility = View.GONE
        rv_search.visibility = View.GONE
    }

    private fun initializeDependencies() {
        ((activity?.application) as MyApp).component().injectSearchFragment(this)
        activity?.let { activity ->
            viewModel =
                ViewModelProvider(activity, viewModelFactory).get(SearchViewModel::class.java)
        }
    }


    //Deselects the item
    private fun deselectMenuItem(view: View) {
        with(view) {
            submenu_layout?.animation =
                AnimationUtils.loadAnimation(context, R.anim.submenu_item_animation_fade_out)
            main_menu_layout?.animation =
                AnimationUtils.loadAnimation(context, R.anim.main_menu_animation_expand)
            submenu_layout?.visibility = View.GONE
            search_btn_add.visibility = View.GONE
        }
    }


    //Contains the logic for searchView
    private fun initializeSearchView() {

        with(search_field) {

            isSubmitButtonEnabled = true

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.getSearchResults(query.trim())
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {

                    //Get new suggestion data, unless just navigated from one
                    if (newText != currentQuery) {
                        currentQuery = newText.trim()
                        viewModel.getSuggestions(currentQuery)
                    }
                    return true
                }
            })
        }
    }


    private fun hideSoftKeyboard(fragment: Fragment) {

        val view = fragment.activity?.currentFocus
        if (view != null) {
            val inputManager: InputMethodManager =
                fragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

