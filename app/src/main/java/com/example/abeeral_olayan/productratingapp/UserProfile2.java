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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile2 extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
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
        editTextPassword = (EditText) findViewById(R.id.editPassword);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("UserAdmin").child(user.getUid()).child("uaname");
        ValueEventListener EventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                editTextName.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addListenerForSingleValueEvent(EventListener);

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
        String password = editTextPassword.getText().toString().trim();
        //empty feild
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(this,"Password length should greater than 6 characters",Toast.LENGTH_LONG).show();
            return;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern))
        {
            Toast.makeText(this,"Invalid email formate", Toast.LENGTH_LONG).show();
            return;
        }

        databaseReference.setValue(name);
        user.updateEmail(email);
        user.updatePassword(password);
        Toast.makeText(this,"information saved...",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,UserHome.class));
    }



}
