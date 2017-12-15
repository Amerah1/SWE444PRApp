package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by abeeral-olayan on 12/11/17.
 */

public class ProductListAdmin extends Fragment{

   String titel;
    private DatabaseReference database;
    private ListView Lprod;
    private ArrayList<String> products;
    //private ArrayAdapter<String> arrayadap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        titel = getArguments().getString("CName");
        return inflater.inflate(R.layout.product_list_admin, container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(titel);


        //start
        products = new ArrayList<String>();
        Lprod = (ListView) view.findViewById(R.id.LProducts);


        database = FirebaseDatabase.getInstance().getReference().child("PRDB");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() == null) {
                    Toast.makeText(getActivity(), "No product", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), AdminHome2.class));
                }
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ImageUploadInfo sd = ds.getValue(ImageUploadInfo.class);
                        if (titel.equals(sd.getPcat())){
                        products.add(sd.getImageName());
                        final ArrayAdapter<String> array;
                        array = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, products);

                        Lprod.setAdapter(array);}
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.addListenerForSingleValueEvent(eventListener);

        try {
            Lprod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String val = (String) parent.getItemAtPosition(position);
                    Toast.makeText(getActivity(), val, Toast.LENGTH_LONG).show();

                    Fragment fr = new ProductInfoAdmin();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle args = new Bundle();
                    args.putString("PName1", val);
                    fr.setArguments(args);
                    ft.replace(R.id.content_frame, fr);
                    ft.commit();

                }
            });


        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

