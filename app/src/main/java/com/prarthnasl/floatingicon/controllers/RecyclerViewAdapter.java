package com.prarthnasl.floatingicon.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prarthnasl.floatingicon.R;
import com.prarthnasl.floatingicon.model.Product;

import java.util.List;

/**
 * Created by prarthnasl on 4/2/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Product> productList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null);
        return new RecyclerViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.productName.setText(productList.get(position).getName());
        holder.productImage.setImageResource(productList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }
}
