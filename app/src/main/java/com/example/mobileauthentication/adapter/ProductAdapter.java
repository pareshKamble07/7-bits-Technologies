package com.example.mobileauthentication.adapter;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileauthentication.R;
import com.example.mobileauthentication.model.Products;
import com.example.mobileauthentication.utils.ProductsSelectedListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.RecyclerViewHolder> {

    private List<Products> productsList;
    private Context mcontext;
    public ProductsSelectedListener productsSelectedListener;

    private Boolean isLongPress = false;

    public ProductAdapter(List<Products> mProductsList, Context mcontext, ProductsSelectedListener productsSelectedListener) {
        this.productsList = mProductsList;
        this.mcontext = mcontext;
        this.productsSelectedListener = productsSelectedListener;
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
        Vibrator vibe = (Vibrator) mcontext.getSystemService(Context.VIBRATOR_SERVICE);

        Picasso.with(mcontext)
                .load(products.getImage())
                .placeholder(R.drawable.user)
                .into(holder.imgV_products);

        if (products.getSelected()) {
            holder.imgV_checks.setVisibility(View.VISIBLE);
        } else {
            holder.imgV_checks.setVisibility(View.GONE);
        }

        holder.relative_bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                vibe.vibrate(50);
                isLongPress = true;

                if (products.getSelected()) {
                    holder.imgV_checks.setVisibility(View.GONE);
                    products.setSelected(false);
                    if (getSelectedProducts().size() == 0) {
                        productsSelectedListener.onItemSelected(false);
                    }
                } else {
                    holder.imgV_checks.setVisibility(View.VISIBLE);
                    products.setSelected(true);
                    productsSelectedListener.onItemSelected(true);
                }
                return true;
            }
        });

        holder.relative_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLongPress) {

                    if (products.getSelected()) {
                        holder.imgV_checks.setVisibility(View.GONE);
                        products.setSelected(false);
                        if (getSelectedProducts().size() == 0) {
                            productsSelectedListener.onItemSelected(false);
                        }
                    } else {
                        holder.imgV_checks.setVisibility(View.VISIBLE);
                        products.setSelected(true);
                        productsSelectedListener.onItemSelected(true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return productsList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgV_products, imgV_checks;
        private RelativeLayout relative_bg;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgV_products = itemView.findViewById(R.id.imgV_products);
            imgV_checks = itemView.findViewById(R.id.imgV_checks);
            relative_bg = itemView.findViewById(R.id.relative_bg);

        }
    }

    public List<Products> getSelectedProducts() {
        List<Products> mProductList = new ArrayList<>();

        for (Products products : productsList) {
            if (products.getSelected()) {
                mProductList.add(products);
            }
        }
        return mProductList;
    }
}

