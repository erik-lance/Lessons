package com.mobdeve.s13.tiongquico.erik.fakeadexexercise

import androidx.recyclerview.widget.RecyclerView

// ViewBinding
import com.mobdeve.s13.tiongquico.erik.fakeadexexercise.databinding.ItemLayoutBinding

class MyViewHolder(private val viewBinding: ItemLayoutBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(model: PokemonModel) {
        this.viewBinding.nameTv.text = model.name
        this.viewBinding.descriptionTv.text = model.desc
        this.viewBinding.locationTv.text = model.location
        this.viewBinding.speciesTv.text = model.specie
        this.viewBinding.imageIv.setImageResource(model.imageId)
    }

    // OnClick for Delete Btn
    fun setDeleteBtnListener(listener: (Int) -> Unit) {
        this.viewBinding.deleteBtn.setOnClickListener {
            listener(this.adapterPosition)
        }
    }
}