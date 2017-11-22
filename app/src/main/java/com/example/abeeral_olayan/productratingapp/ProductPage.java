package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductPage extends AppCompatActivity {

    //defining view objects
    private RatingBar ratingBar;
    private Button addRating;
    private ImageView imageView;
    //defining firebaseauth object

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    private int newRate=0;
    private ImageUploadInfo Rate;
    //product opject
    private String Pcat;
    private String Pdesc;
    private String Pprice;
    private String imageName;
    private String imageURL;
    private int numOfRating=0;
    private double rating=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        try{
            //initializing views
            ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            addRating = (Button) findViewById(R.id.button);
            imageView = (ImageView) findViewById(R.id.imageView);
            String url="https://firebasestorage.googleapis.com/v0/b/productratingapp.appspot.com/o/view%20receipt.png?alt=media&token=2af33f57-68ba-4697-b623-c482bc9d03b7";
            //Glide.with(getApplicationContext().load(url).into(imageView));
            databaseReference= FirebaseDatabase.getInstance().getReference().child("PRDB").child("Pinfo").child("Dior");
            /*try{
                ImageUploadInfo p = new ImageUploadInfo("other", "this good", "50", "tv", "https://url", "0", "0");
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("PRDB").child("Pinfo").child("Dior2").setValue(p);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }*/
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

            //databaseReference2.child("PRDB").child("Pinfo").child("pro1");
            addRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    startActivity(new Intent(getApplicationContext(), AdminHome2.class));

                }
            });

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    Toast.makeText(getApplicationContext(), "Rate star " + v, Toast.LENGTH_SHORT).show();
                    newRate = (int) v;
                }
            });

        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }
}
