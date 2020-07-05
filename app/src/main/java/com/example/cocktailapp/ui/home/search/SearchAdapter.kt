package com.example.cocktailapp.ui.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailapp.R
import com.example.cocktailapp.SMALL_IMAGE_ADDITION
import com.example.cocktailapp.data.model.Drink
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(
    private val list: MutableList<Drink>,
    private val onCocktailClick: (Drink) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.populateList(R.layout.search_item), onCocktailClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAllData(list: List<Drink>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View, private val onCocktailClick: (Drink) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(drink: Drink) {

            itemView.tv_name.text = drink.strDrink

            val fullDrinkUrl = drink.strDrinkThumb + SMALL_IMAGE_ADDITION
            Picasso.get().load(fullDrinkUrl).into(itemView.iv_thumb)

            itemView.setOnClickListener {
                onCocktailClick.invoke(drink)
            }
        }
    }

    private fun ViewGroup.populateList(resourceId: Int): View {

        return this.let { LayoutInflater.from(context).inflate(resourceId, it, false) }

    }
}