package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserHome extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private Button buttonProfile;
    private Button SuggestP;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;
    ProgressDialog progressDialog;
    List<ImageUpload_Category> listCategories = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        setTitle("User Home");


        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();


        buttonLogout= (Button) findViewById(R.id.logout);
        buttonProfile= (Button) findViewById(R.id.uprofile);
        SuggestP= (Button) findViewById(R.id.SuggestP);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(UserHome.this));

        progressDialog = new ProgressDialog(UserHome.this);
        progressDialog.setMessage("Categories are Loading...");
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        databaseReference = FirebaseDatabase.getInstance().getReference("CATDB");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUpload_Category catInfo = postSnapshot.getValue(ImageUpload_Category.class);

                    listCategories.add(catInfo);
                }

                // calling RecyclerViewAdapter.java
                adapter = new RecyclerViewAdapter(getApplicationContext(), listCategories);
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

        // buttons
        buttonLogout.setOnClickListener(this);
        buttonProfile.setOnClickListener(this);
        SuggestP.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v == buttonProfile) {
            finish();
            startActivity(new Intent(this , UserProfile2.class));
        }
        else
            if (v == SuggestP)
                startActivity(new Intent(this , Suggest_products.class));

            else{
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}