package com.prarthnasl.floatingicon.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prarthnasl.floatingicon.R;

/**
 * Created by prarthnasl on 4/2/2016.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

    public TextView productName;
    public ImageView productImage;

    public RecyclerViewHolder(View itemView) {
        super(itemView);

        productName = (TextView) itemView.findViewById(R.id.product_name);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(v.getContext(), "Long click", Toast.LENGTH_SHORT).show();
        return false;
    }
}
