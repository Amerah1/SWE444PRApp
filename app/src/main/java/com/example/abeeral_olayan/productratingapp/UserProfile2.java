package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile2 extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonEdit, buttonCancle;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);
        setTitle("My profile");

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

        editTextName = (EditText) findViewById(R.id.editName);
        editTextEmail = (EditText) findViewById(R.id.editEmail);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //
        editTextEmail.setText(user.getEmail());

        ////////
        buttonEdit= (Button) findViewById(R.id.edit);
        buttonCancle= (Button) findViewById(R.id.cancle);

        buttonEdit.setOnClickListener(this);
        buttonCancle.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        if(v == buttonEdit) {
            EditUserInfo();

        }else{
            finish();
            //starting login activity
            startActivity(new Intent(this, UserHome.class));
        }
    }

    private void EditUserInfo() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        //empty feild
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter name",Toast.LENGTH_LONG).show();
            return;
        }

        databaseReference.child("UserAdmin").child(user.getUid()).child("uaname").setValue(name);
        user.updateEmail(email);
        Toast.makeText(this,"information saved...",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,UserHome.class));
    }



}
