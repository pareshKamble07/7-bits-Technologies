package com.example.mobileauthentication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mobileauthentication.adapter.ProductAdapter;
import com.example.mobileauthentication.databinding.ActivityProductsBinding;
import com.example.mobileauthentication.model.Products;
import com.example.mobileauthentication.utils.ProductsSelectedListener;
import com.example.mobileauthentication.utils.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ProductsSelectedListener {

    ActivityProductsBinding binding;
    DatabaseReference databaseReference;
    List<Products> productsList = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_products);

        binding = ActivityProductsBinding.inflate(getLayoutInflater());

        progressDialog = new ProgressDialog(ProductsActivity.this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);

        // getting our root layout in our view.
        View view = binding.getRoot();

        // below line is to set
        // Content view for our layout.
        setContentView(view);

        if (Util.isNetworkConnected(this)) {
            get_productData();
        } else {
            Util.showToastMessage(this, "No Internet Connection.");
        }

    }

    private void get_productData() {

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();
                productsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.e("onDataChange: ", postSnapshot.toString());

                    Products products = postSnapshot.getValue(Products.class);
                    productsList.add(products);
                }

                ProductAdapter adapter = new ProductAdapter(productsList, ProductsActivity.this,ProductsActivity.this);
                binding.rvProductsItems.setLayoutManager(new GridLayoutManager(ProductsActivity.this, 3));
                binding.rvProductsItems.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.cancel();
                Util.showToastMessage(ProductsActivity.this, databaseError.getMessage());
            }
        });

    }

    @Override
    public void onItemSelected(Boolean isSelected) {

    }
}