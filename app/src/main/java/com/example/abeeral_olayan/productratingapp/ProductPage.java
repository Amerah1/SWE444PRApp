package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductPage extends AppCompatActivity implements View.OnClickListener{

    //defining view objects
    private RatingBar ratingBar;
    private Button addRating;
    private EditText editComment;
    private ListView listComments;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference DatabaseListComent;

    private ArrayList<String> ListComment;
    private int newRate=0;
    private ImageUploadInfo Rate;
    private String Pcat;
    private String Pdesc;
    private String Pprice;
    private String imageName;
    private String imageURL;
    private int numOfRating=0;
    private double rating=0.0;
    private int numOfComments=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String productName = getIntent().getExtras().getString("productName");
        setContentView(R.layout.activity_product_page);
            //initializing views
            ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            addRating = (Button) findViewById(R.id.button);
            editComment= (EditText) findViewById(R.id.editComment);
            listComments= (ListView) findViewById(R.id.listComment);
            ListComment = new ArrayList<String>();
            //rating a product/////////////////////////////////////////////////////
            databaseReference= FirebaseDatabase.getInstance().getReference().child("PRDB").child(productName);
            ValueEventListener EventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Rate = dataSnapshot.getValue(ImageUploadInfo.class);
                    Pcat= Rate.getPcat();
                    Pdesc= Rate.getPdesc();
                    Pprice= Rate.getPprice();
                    imageName= Rate.getImageName();
                    imageURL= Rate.getImageURL();
                    numOfRating = Integer.parseInt(Rate.getNumOfRating());
                    rating = Double.parseDouble(Rate.getRating());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            databaseReference.addListenerForSingleValueEvent(EventListener);
            // select star///////////////////////////////
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    Toast.makeText(getApplicationContext(), "Rate stars: " + v, Toast.LENGTH_SHORT).show();
                    newRate = (int) v;
                }
            });
            //List comments/////////////////////////////////////
            DatabaseListComent = FirebaseDatabase.getInstance().getReference().child("Comments").child(productName);
            final ValueEventListener eventListener3 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildren()==null){
                        Toast.makeText(getApplicationContext(),"No comments",Toast.LENGTH_LONG).show();
                    }
                    for (DataSnapshot ds : dataSnapshot.getChildren() ) {
                        String comment= ds.getValue(String.class);
                        ListComment.add(comment);
                        //suggested.add(sd.getImageName());
                        final ArrayAdapter<String> array;
                        array = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ListComment);
                        listComments.setAdapter(array);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            DatabaseListComent.addListenerForSingleValueEvent(eventListener3);
            // add comment //////////////////////////////////
            editComment.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int i, KeyEvent Event) {
                    if ((Event.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                        databaseReference2= FirebaseDatabase.getInstance().getReference().child("Comments").child(productName);
                        ValueEventListener EventListener1 = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                numOfComments= (int) dataSnapshot.getChildrenCount();
                                if(!editComment.getText().toString().equals("")) {
                                    FirebaseDatabase.getInstance().getReference().child("Comments").child(productName).child("comment" + (numOfComments + 1)).setValue(editComment.getText().toString());
                                    Intent intent = new Intent(getApplicationContext(), ProductPage.class);
                                    intent.putExtra("productName",productName);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        };
                        databaseReference2.addListenerForSingleValueEvent(EventListener1);
                        return true;
                    }
                    return false;
                }
            });
            //list view ///////////////////////////////
            addRating.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) { if(view == addRating){ rating(); } }

    private void rating() {
        numOfRating++;
        rating = rating + (double) newRate;
        rating = rating / (double) numOfRating;
        Rate.setPcat(Pcat);
        Rate.setPdesc(Pdesc);
        Rate.setPprice(Pprice);
        Rate.setImageName(imageName);
        Rate.setImageURL(imageURL);
        Rate.setNumOfRating(""+numOfRating);
        Rate.setRating(""+rating);
        databaseReference.setValue(Rate);
    }
}

