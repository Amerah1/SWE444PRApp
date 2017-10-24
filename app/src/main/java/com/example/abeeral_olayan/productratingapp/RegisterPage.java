package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private String email;
    private String password;
    //add
    private EditText AUName;
    private EditText passAgain;
    private DatabaseReference mDatabase;
    private TextView textViewLogin;
    //
    private Button buttonSignup;
    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewLogin = (TextView) findViewById(R.id.textViewSignin);
        //Add
        AUName = (EditText) findViewById(R.id.editTextName);
        passAgain = (EditText) findViewById(R.id.editTextPasswordAgain);
        //


        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        email = editTextEmail.getText().toString().trim();
        password  = editTextPassword.getText().toString().trim();
        String name = AUName.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        //Add
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter Name",Toast.LENGTH_LONG).show();
            return;
        }

        if(!editTextPassword.getText().toString().equals(passAgain.getText().toString())){
            Toast.makeText(this,"password doesn't match Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        //

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){

                            //Add
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();


                            UserAdmin userAdmin= new UserAdmin(uid,AUName.getText().toString());
                            mDatabase= FirebaseDatabase.getInstance().getReference();
                            //mDatabase.child("users").child(uid).setValue(user);

                            mDatabase.child("UserAdmin").child(uid).setValue(userAdmin);

                            //display some message here
                            Toast.makeText(RegisterPage.this,"Successfully registered",Toast.LENGTH_LONG).show();


                        }else{
                            //display some message here
                            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                            if (email.matches(emailPattern))
                            {
                                Toast.makeText(getApplicationContext(),"email address is already exists",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Invalid email formate", Toast.LENGTH_LONG).show();
                            }
                            if(password.length()<6){
                                Toast.makeText(getApplicationContext(),"Password length should ",Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        if(view == buttonSignup) {
            registerUser();
        }else{
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
        }
    }

}
