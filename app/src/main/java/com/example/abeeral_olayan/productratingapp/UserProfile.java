package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by abeeral-olayan on 11/7/17.
 */

public class UserProfile extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonEdit;
    private String name;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ufragment_user_profile,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //fot title
        getActivity().setTitle("Profile");

        editTextName = (EditText) view.findViewById(R.id.editName);
        editTextEmail = (EditText) view.findViewById(R.id.editEmail);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //
        editTextEmail.setText(user.getEmail());

        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View view){
                EditUserInfo();
            }

        });

        view.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserHome2.class));
            }
        });
    }

    private void EditUserInfo() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        //empty feild
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(), "Please enter name",Toast.LENGTH_LONG).show();
            return;
        }

        databaseReference.child("UserAdmin").child(user.getUid()).child("uaname").setValue(name);
        user.updateEmail(email);
        Toast.makeText(getActivity(),"information saved...",Toast.LENGTH_LONG).show();
        startActivity(new Intent(getActivity(),UserHome2.class));
    }
}
