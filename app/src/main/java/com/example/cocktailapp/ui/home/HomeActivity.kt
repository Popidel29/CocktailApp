package com.example.cocktailapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cocktailapp.R
import com.example.cocktailapp.ui.home.search.SearchFragment

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                SearchFragment()
            ).commit()
        }

    }
}