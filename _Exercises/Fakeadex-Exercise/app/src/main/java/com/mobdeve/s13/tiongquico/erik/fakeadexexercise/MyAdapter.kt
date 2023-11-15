package com.mobdeve.s13.tiongquico.erik.fakeadexexercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.tiongquico.erik.fakeadexexercise.databinding.ItemLayoutBinding

class MyAdapter(private val pokemonList: ArrayList<PokemonModel>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemViewBinding: ItemLayoutBinding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return this.pokemonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(this.pokemonList[position])

        // OnClick for Delete Btn
        holder.setDeleteBtnListener {
            this.pokemonList.removeAt(position)
            this.notifyItemRemoved(position)
        }
    }

}