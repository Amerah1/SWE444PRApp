package com.example.abeeral_olayan.productratingapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lamia on 03/11/17.
 */

public class profile extends Fragment {
    private FirebaseAuth firebaseAuth;

    //view objects
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button ButtonEdit;


    private String name;
    private String email;
    private String password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("profile");

        editTextName=(EditText) view.findViewById(R.id.editName);
        editTextEmail=(EditText) view.findViewById(R.id.editEmail);
        editTextPassword= (EditText) view.findViewById(R.id.editPassword);

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

        //to create listner, update info
        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditAdminInfo();
                Fragment fr = new ListCategories();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.content_frame, fr);
                ft.commit();
            }
        });
        view.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fr = new ListCategories();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.content_frame, fr);
                ft.commit();
            }
        });

    }

    private void EditAdminInfo(){
        name = editTextName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        //empty feild
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(),"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(getActivity(),"Password length should greater than 6 characters",Toast.LENGTH_LONG).show();
            return;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern))
        {
            Toast.makeText(getActivity(),"Invalid email formate", Toast.LENGTH_LONG).show();
            return;
        }

        databaseReference.setValue(name);
        user.updateEmail(email);
        user.updatePassword(password);
        Toast.makeText(getActivity(), "information saved...", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getActivity(), AdminHome2.class));
    }


}
