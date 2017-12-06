package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListProducts extends Fragment {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;
    ProgressDialog progressDialog;
    List<ImageUploadInfo> list_product = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Products");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Products are Loading...");
        progressDialog.show();

        // Setting up Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PRDB");

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Bundle extras = getActivity().getIntent().getExtras();
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
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }

                adapter = new RecyclerViewAdapter_products(getActivity(), list_product);

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
