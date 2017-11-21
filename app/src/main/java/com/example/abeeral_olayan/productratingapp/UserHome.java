package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserHome extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private Button buttonProfile;
    private Button SuggestP;


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
