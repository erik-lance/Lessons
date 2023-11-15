package com.mobdeve.tighee.fakeadex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.tighee.fakeadex.databinding.ItemLayoutBinding

class MyAdapter(private val data: ArrayList<PokemonModel>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // This is a way to perform View Binding in the RecyclerView.
        val itemViewBinding: ItemLayoutBinding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return MyViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Pass data into the ViewHolder using a single binding method
        holder.bindData(this.data[position])

        // Give the delete button logic (Toast + delete data + update view)
        // OnClickListener is defined here so that it has access to the data and passed into the ViewHolder
        holder.setDeleteOnClickListener(View.OnClickListener {
            // Inform the user of the deleted element
            Toast.makeText(
                holder.itemView.context,
                this.data[position].name + " has been deleted.",
                Toast.LENGTH_SHORT
            ).show()

            // Remove the element from the data (i.e. ArrayList)
            this.data.removeAt(position)

            // Inform the adapter class that the data has changed
            // notifyDataSetChanged() -> This forces the RecyclerView to update but it is computationally costly
            notifyItemRemoved(position) // This is more appropriate for our logic vs a complete dataset change
            // Without the previous line, the view won't update despite the data changing
        })
    }

    override fun getItemCount(): Int {
        return this.data.size
    }
}