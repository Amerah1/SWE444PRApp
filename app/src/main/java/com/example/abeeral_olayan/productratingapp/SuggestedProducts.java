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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by abeeral-olayan on 11/16/17.
 */

public class SuggestedProducts extends Fragment {


    private DatabaseReference database;
    private ListView Lpro;
    private ArrayList<String> suggested;
    //private ArrayAdapter<String> arrayadap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_suggested_products, container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Suggested Products");


        //start
        suggested=new ArrayList<String>();
        Lpro = (ListView) view.findViewById(R.id.LProduct);


        database = FirebaseDatabase.getInstance().getReference().child("SPRDB");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren()==null){
                    Toast.makeText(getActivity(),"No suggested product",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), AdminHome2.class));
                }
                try { for (DataSnapshot ds : dataSnapshot.getChildren() ) {
                       // ImageUploadInfo sd = ds.getValue(ImageUploadInfo.class);
                          SuggestProducctInfo sd= ds.getValue(SuggestProducctInfo.class);
                          suggested.add(sd.getSPName());
                        //suggested.add(sd.getImageName());
                        final ArrayAdapter<String> array;
                        array = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, suggested);

                        Lpro.setAdapter(array);
                }} catch (Exception e){
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

          database.addListenerForSingleValueEvent(eventListener);

           try {


            Lpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Intent intent=new Intent(getActivity(),AcseptReject.class);
                    //intent.putExtra("ProductName", suggested.toString());
                    //startActivity(intent);

                    //Put the value
                    String val =(String) parent.getItemAtPosition(position);

                    Fragment fr=new AcseptReject();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    Bundle args = new Bundle();
                    args.putString("PName", val);
                    fr.setArguments(args);
                    ft.replace(R.id.content_frame, fr);
                    ft.commit();


                    //AcseptReject ldf = new AcseptReject ();
                    //Bundle args = new Bundle();
                    //args.putString("PName", val);
                    //ldf.setArguments(args);

                    //Inflate the fragment
                    //getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();


                    //Toast.makeText(getActivity().getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),val,Toast.LENGTH_LONG).show();



                }
            });} catch (Exception e) {
               Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
           }



    }



}
