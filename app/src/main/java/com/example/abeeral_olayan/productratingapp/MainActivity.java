package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button buttonSignUp , buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSignUp= (Button) findViewById(R.id.signup_but);
        buttonSignIn= (Button) findViewById(R.id.signin_but);

        buttonSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);


    }




    @Override
    public void onClick(View v) {
        if(v == buttonSignUp) {
            finish();
            startActivity(new Intent(this , RegisterPage.class));
        }else{
            finish();
            startActivity(new Intent(this , LoginPage.class));
        }
    }

}
