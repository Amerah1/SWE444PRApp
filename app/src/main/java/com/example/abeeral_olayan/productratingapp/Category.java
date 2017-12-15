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
 * Created by abeeral-olayan on 12/13/17.
 */

public class Category extends Fragment {


    private DatabaseReference database;
    private ListView Lcat;
    private ArrayList<String> category;
    //private ArrayAdapter<String> arrayadap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fraf, container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle("Suggested Products");


        //start
        category=new ArrayList<String>();
        Lcat = (ListView) view.findViewById(R.id.LCategory);


        database = FirebaseDatabase.getInstance().getReference().child("CATDB");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren()==null){
                    Toast.makeText(getActivity(),"No suggested product",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), AdminHome2.class));
                }
                try { for (DataSnapshot ds : dataSnapshot.getChildren() ) {
                    // ImageUploadInfo sd = ds.getValue(ImageUploadInfo.class);
                    ImageUpload_Category sd= ds.getValue(ImageUpload_Category.class);
                    category.add(sd.getImageName());
                    //suggested.add(sd.getImageName());
                    final ArrayAdapter<String> array;
                    array = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, category);

                    Lcat.setAdapter(array);
                }} catch (Exception e){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.addListenerForSingleValueEvent(eventListener);

        Lcat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ti= (String) parent.getItemAtPosition(position);
                //Toast.makeText(getActivity(),ti,Toast.LENGTH_LONG).show();

              /* Fragment fra= new ProductListAdmin();
                FragmentManager fma= getFragmentManager();
                FragmentTransaction ftr= fma.beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("CName",ti);
                fra.setArguments(bundle);
                ftr.replace(R.id.content_frame,fra);
                ftr.commit();*/
                //Put the value
              try {
                  Fragment ldf = new ProductListAdmin ();

                Bundle args = new Bundle();
                args.putString("CName", ti);
                ldf.setArguments(args);

//Inflate the fragment
                getFragmentManager().beginTransaction().add(R.id.content_frame, ldf).commit();}
                catch (Exception e){
                  Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });




    }



}

