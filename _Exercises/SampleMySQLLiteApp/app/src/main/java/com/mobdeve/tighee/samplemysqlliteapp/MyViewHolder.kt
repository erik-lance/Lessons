package com.mobdeve.tighee.samplemysqlliteapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView
    private val numberTv: TextView
    private val imageIv: ImageView

    init {
        nameTv = itemView.findViewById(R.id.nameTv)
        numberTv = itemView.findViewById(R.id.numberTv)
        imageIv = itemView.findViewById(R.id.imageIv)
    }

    fun bindData(c: Contact) {
        nameTv.text = c.lastName + ", " + c.firstName
        numberTv.text = c.number
        Picasso.get()
            .load(c.imageUri)
            .into(imageIv)
    }
}