package com.mobdeve.tighee.fakeadex

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.tighee.fakeadex.databinding.ItemLayoutBinding

/*  By using the View Binding of the item's layout, we reduce the amount of code needed in the
    ViewHolder. Please note that instead of passing in the itemView, we pass in the View Binding.
    Additionally, instead of passing the itemView to the ViewHolder's constructor, we pass in the
    root view of the viewBinding.
* */
class MyViewHolder(private val viewBinding: ItemLayoutBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(model: PokemonModel) {
        this.viewBinding.nameTv.text = model.name
        this.viewBinding.descriptionTv.text = model.desc
        this.viewBinding.locationTv.text = model.location
        this.viewBinding.speciesTv.text = model.specie
        this.viewBinding.imageIv.setImageResource(model.imageId)
    }

    fun setDeleteOnClickListener(onClickListener: View.OnClickListener) {
        this.viewBinding.deleteBtn.setOnClickListener(onClickListener)
    }
}