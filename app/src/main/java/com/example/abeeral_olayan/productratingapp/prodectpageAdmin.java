package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class prodectpageAdmin extends AppCompatActivity {
    public  static final String Database_Path="All_Image_Uploads_Database";//-----------()____________________
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase3;
    private DatabaseReference mDatabase4;
    private TextView PName;
    private TextView  PpriceTEXT;
    private TextView  PdescTEXT;
    private Spinner Pcat;
    private ImageView imageView;
    private ArrayList<String> categories;
    private Button Upload_image;
    private DatabaseReference databaseReference;
    private FirebaseUser user;//add it
    private  ImageUploadInfo ob;//-----------()---------------
    private ImageUploadInfo ob2;
    private String TempImageName;

    //private ImageView Pimage;

    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference ref;
    DatabaseReference mostafa;

    int Image_Request_Code = 7;
    ProgressDialog progressDialog;


    // Folder path for Firebase Storage.
    private String Storage_Path = "Products Images";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////final String productName = getIntent().getExtras().getString("productName");///////

        setContentView(R.layout.activity_prodectpage_admin);
        setTitle("Product Page for admin");

        //////////////////
        //  Spinner
        PName=(TextView )findViewById(R.id.PName);
        PpriceTEXT=(TextView ) findViewById(R.id.Pprice);
        PdescTEXT=(TextView ) findViewById(R.id.Pdesc);
        Pcat = (Spinner) findViewById(R.id.spinner);
        //Upload_image = (Button) findViewById(R.id.Pimage);
        imageView=(ImageView) findViewById(R.id.imageView5);
        /////////////////


        categories = new ArrayList<String>();
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("CATDB");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren() ) {
                    ImageUpload_Category category = ds.getValue(ImageUpload_Category.class);
                    categories.add(category.getImageName());
                    final ArrayAdapter<String> arrayadap;
                    arrayadap = new ArrayAdapter<String>(prodectpageAdmin.this, android.R.layout.simple_spinner_item, categories);
                    Pcat = (Spinner) findViewById(R.id.spinner);
                    Pcat.setAdapter(arrayadap);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase2.addListenerForSingleValueEvent(eventListener);
        // End Spinner code
        ///////////////////
        user = FirebaseAuth.getInstance().getCurrentUser();
        try{

            mDatabase1 = FirebaseDatabase.getInstance().getReference();
            ref =  mDatabase1.child("PRDB").child("pp");////////////////change accroding to prodect page


            ValueEventListener EventListener2 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ob = dataSnapshot.getValue(ImageUploadInfo.class);
                    try {
                        //if(ob.getPprice()!=null)
                        PpriceTEXT.setText(ob.getPprice());
                        //if(ob.getPdesc()!=null)
                        PdescTEXT.setText(ob.getPdesc());
                        //if(ob.getPcat()!=null)
                        Pcat.setSelection(getIndex(Pcat, ob.getPcat()));
                        //if(ob.getImageURL()!=null)__________________()-----------------------
                        Picasso.with(getApplicationContext()).load(ob.getImageURL()).placeholder(R.mipmap.ic_launcher).into(imageView);// add now----------
                        //if(ob.getImageURL()!=null)
                        //FilePathUri= Uri.parse(ob.getImageURL());
                        //Upload_image.setPressed(true);
                        //if(ob.getImageName()!=null)
                        PName.setText(ob.getImageName());
                    }catch (Exception e){
                        Toast.makeText(prodectpageAdmin.this,e.getMessage(),Toast.LENGTH_LONG).show();}

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            ref.addListenerForSingleValueEvent(EventListener2);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        ///////////////////



        findViewById(R.id.AddPB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(prodectpageAdmin.this, EditProdectInfoN.class));
            }
        });
        findViewById(R.id.CanclePB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startActivity(new Intent(prodectpageAdmin.this, AdminHome2.class));//---------------()++++++++++++++++++
            }
        });





    }//creat

    private int getIndex(Spinner pcat, String pcat1) {
        int index = 0;

        for (int i=0;i<pcat.getCount();i++){
            if (pcat.getItemAtPosition(i).equals(pcat1)){
                index = i;
            }
        }
        return index;
    }


}
