package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class Suggest_products extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private Button buttonProfile;
    private Button SuggestP;
    private Button homep;
    private Button AddSP;
    private DatabaseReference mDatabase2;
    private EditText SPName;
    private EditText SPprice;
    private EditText SPdesc;
    private Spinner SPcat;
    private ArrayList<String> SPcategories;
    private Button Upload_image_SP;
    private Button cancle;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;

    // Folder path for Firebase Storage.
    String Storage_Path = "Suggested Products Images";



        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_products);
            setTitle("Suggest Product");

        // Start Spinner code

        SPcategories = new ArrayList<String>();
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("CATDB");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ImageUpload_Category category = ds.getValue(ImageUpload_Category.class);
                    SPcategories.add(category.getImageName());
                    final ArrayAdapter<String> arrayadap;
                    arrayadap = new ArrayAdapter<String>(Suggest_products.this, android.R.layout.simple_spinner_item, SPcategories);
                    SPcat = (Spinner) findViewById(R.id.spinner);
                    SPcat.setAdapter(arrayadap);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase2.addListenerForSingleValueEvent(eventListener);
        // End Spinner code


// Start Image code

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SPRDB");
            buttonLogout= (Button) findViewById(R.id.logout2);
            buttonProfile= (Button) findViewById(R.id.uprofile2);
            SuggestP= (Button) findViewById(R.id.SuggestP2);
            homep= (Button)  findViewById(R.id.home2);
        AddSP = (Button) findViewById(R.id.AddSPB);
        SPName = (EditText) findViewById(R.id.PName);
        SPprice = (EditText) findViewById(R.id.Pprice);
        SPdesc = (EditText) findViewById(R.id.Pdesc);
        SPcat = (Spinner) findViewById(R.id.spinner);
        Upload_image_SP = (Button) findViewById(R.id.Pimage);
        cancle = (Button) findViewById(R.id.CancleSB);
        progressDialog = new ProgressDialog(Suggest_products.this);
        // Adding click listener to Choose image button.
        Upload_image_SP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        // Adding click listener to Upload image button.
        AddSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Suggest_products.this, UserHome.class));
            }
        });

            SuggestP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Calling method to upload selected image on Firebase storage.
                    startActivity(new Intent(Suggest_products.this, Suggest_products.class));

                }
            });

            buttonProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Suggest_products.this, UserProfile2.class));

                }
            });
            /*
            homep.setOnClickListener(new View.OnClickListener() {////////////////////////+++++++++++++++++++++++++++????????????+++++++++++
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Suggest_products.this, UserHome.class));

                }
            });
            */
            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    //logging out the user
                    firebaseAuth.signOut();
                    //closing activity
                    finish();
                    //starting login activity
                    startActivity(new Intent(Suggest_products.this, MainActivity.class));

                }
            });
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);


                // After selecting image change choose button above text.
                Upload_image_SP.setText("Image Selected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        // Creating Method to get the selected image file Extension from File Path URI.

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null && !TextUtils.isEmpty(SPName.getText().toString().trim())) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Sending Request...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String SPName1 = SPName.getText().toString().trim();
                            String SPdesc1 = SPdesc.getText().toString().trim();
                            String SPprice1 = SPprice.getText().toString().trim();
                            String SPcat1 = SPcat.getSelectedItem().toString().trim();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();
                            try
                            {
                            @SuppressWarnings("VisibleForTests")
                            SuggestProducctInfo SuggestProducctInfo = new SuggestProducctInfo(SPName1, taskSnapshot.getDownloadUrl().toString(), SPdesc1, SPprice1, SPcat1);
                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(SPName.getText().toString()).setValue(SuggestProducctInfo);
                                // Showing toast message after done uploading.
                                Toast.makeText(getApplicationContext(), "Product Suggested Successfully ", Toast.LENGTH_LONG).show();
                                SPName.setText("");
                                SPdesc.setText("");
                                SPprice.setText("");
                            }
                            catch (Exception e){
                            Toast.makeText(Suggest_products.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }}
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(Suggest_products.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Sending Request...");

                        }
                    });
        }

        if (TextUtils.isEmpty(SPName.getText().toString().trim()) && FilePathUri == null)
            Toast.makeText(this, "Please set a name and select image for the product.", Toast.LENGTH_LONG).show();


        if(TextUtils.isEmpty(SPName.getText().toString().trim()) && FilePathUri != null )
            Toast.makeText(this, "Please set a name for the product.", Toast.LENGTH_LONG).show();

        if (FilePathUri == null && !TextUtils.isEmpty(SPName.getText().toString().trim()))
            Toast.makeText(this, "Please select image for the product.", Toast.LENGTH_LONG).show();

    }
            }