package com.example.mobileauthentication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileauthentication.R;
import com.example.mobileauthentication.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.RecyclerViewHolder> {

    private List<Products> productsList;
    private Context mcontext;

    public ProductAdapter(List<Products> mProductsList, Context mcontext) {
        this.productsList = mProductsList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        Products products = productsList.get(position);


        Picasso.with(mcontext)
                .load(products.getImage())
                .placeholder(R.drawable.user)
                .into(holder.imgV_products);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return productsList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgV_products;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgV_products = itemView.findViewById(R.id.imgV_products);

        }
    }
}

