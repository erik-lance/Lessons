package com.mobdeve.tighee.samplesqliteapp;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTv, numberTv;
    private ImageView imageIv;
    private ImageButton deleteBtn, editBtn;

    public MyViewHolder(View itemView) {
        super(itemView);

        this.nameTv = itemView.findViewById(R.id.nameTv);
        this.numberTv = itemView.findViewById(R.id.numberTv);
        this.imageIv = itemView.findViewById(R.id.imageIv);
        this.deleteBtn = itemView.findViewById(R.id.deleteBtn);
        this.editBtn = itemView.findViewById(R.id.editBtn);
    }

    public void bindData(Contact c) {
        this.nameTv.setText(c.getLastName() + ", " + c.getFirstName());
        this.numberTv.setText(c.getNumber());
        Log.d("TAG", "bindData: " + c.toString());
        Picasso.get()
            .load(c.getImageUri())
            .into(this.imageIv);
    }

    public void setDeleteBtnOnClickListener(View.OnClickListener onClickListener) {
        this.deleteBtn.setOnClickListener(onClickListener);
    }

    public void setEditBtnOnClickListener(View.OnClickListener onClickListener) {
        this.editBtn.setOnClickListener(onClickListener);
    }
}
