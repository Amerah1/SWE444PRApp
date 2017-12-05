package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsView extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;
    ProgressDialog progressDialog;
    List<ImageUploadInfo> list_product = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_view);
        setTitle("Products");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsView.this));

        progressDialog = new ProgressDialog(ProductsView.this);
        progressDialog.setMessage("Products are Loading...");
        progressDialog.show();

        // Setting up Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PRDB");

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Bundle extras = getIntent().getExtras();
                String getCatName = (String) extras.get("CatName");
                ImageUploadInfo proInfo;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    proInfo = postSnapshot.getValue(ImageUploadInfo.class);

try {
    if (getCatName.equals(proInfo.getPcat())) {
        list_product.add(proInfo);
    }
}

catch (Exception e){
    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
}

                }

                adapter = new RecyclerViewAdapter_products(getApplicationContext(), list_product);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });

    }
}