package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
    private Button ButtonEdit;

    private String name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        return inflater.inflate(R.layout.fragment_nav_profile, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("profile");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName=(EditText) view.findViewById(R.id.editName);
        editTextEmail=(EditText) view.findViewById(R.id.editEmail);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //https://product-rating-app.firebaseio.com ,,,, ref

        editTextName.setText(user.getDisplayName());
        editTextEmail.setText(user.getEmail());

        //to create listner, update info
        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditAdminInfo();
            }
        });
        view.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AdminHome2.class));
            }
        });

    }

    private void EditAdminInfo(){
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        //empty feild
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(),"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("UserAdmin").child(user.getUid()).child("uaname").setValue(name);
        user.updateEmail(email);
        Toast.makeText(getActivity(), "information saved...", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getActivity(), AdminHome2.class));
    }
}
