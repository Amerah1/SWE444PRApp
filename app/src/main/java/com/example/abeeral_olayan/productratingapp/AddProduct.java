package com.example.abeeral_olayan.productratingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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

import static android.app.Activity.RESULT_OK;


/**
 * Created by leenah on 11/7/17.
 */

public class AddProduct extends Fragment {


    private Button AddP;
    private DatabaseReference mDatabase2;
    private EditText PName;
    private EditText Pprice;
    private EditText Pdesc;
    private Spinner Pcat;
    private ArrayList<String> categories;
    private Button Upload_image;
    private Button cancle;

    //private ImageView Pimage;

    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;


    // Folder path for Firebase Storage.
    String Storage_Path = "Products Images/";


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Add Product");

        // Start Spinner code

        categories = new ArrayList<String>();
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("CATDB");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren() ) {
                    ImageUpload_Category category = ds.getValue(ImageUpload_Category.class);
                    categories.add(category.getImageName());
                    final ArrayAdapter<String> arrayadap;
                    arrayadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
                    Pcat = (Spinner) view.findViewById(R.id.spinner);
                    Pcat.setAdapter(arrayadap);

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("PRDB").child("PInfo");
        AddP = (Button) view.findViewById(R.id.AddPB);
        PName = (EditText) view.findViewById(R.id.PName);
        Pprice = (EditText) view.findViewById(R.id.Pprice);
        Pdesc = (EditText) view.findViewById(R.id.Pdesc);
        Pcat = (Spinner) view.findViewById(R.id.spinner);
        Upload_image = (Button) view.findViewById(R.id.Pimage);
        //Pimage = (ImageView) view.findViewById(R.id.imageView);
        cancle = (Button) view.findViewById(R.id.CanclePB);
        //////////////
        progressDialog = new ProgressDialog(getActivity());
        // Adding click listener to Choose image button.
        Upload_image.setOnClickListener(new View.OnClickListener() {
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
        AddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AdminHome2.class));
                //startActivity(new Intent(getContext(), ProductPage.class));

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_product, container,false);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                //Pimage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                Upload_image.setText("Image Selected");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null && !TextUtils.isEmpty(PName.getText().toString().trim())) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Product is Adding...");

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
                            String TempImageName = PName.getText().toString().trim();
                            String Pdesc1 = Pdesc.getText().toString().trim();
                            String Pprice1 = Pprice.getText().toString().trim();
                            String Pcat1 = Pcat.getSelectedItem().toString().trim();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            ///////////////////
                            Toast.makeText(getActivity(), "Product Added Successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString() ,Pdesc1,Pprice1,Pcat1,"0","0");

                            // Getting image upload ID.
                            //String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(PName.getText().toString()).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            /////////////////////////////
                            Toast.makeText(getActivity() , exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Product is Adding...");

                        }
                    });
        }

        if (TextUtils.isEmpty(PName.getText().toString().trim()) && FilePathUri == null)
            Toast.makeText(getActivity(), "Please set a name and select image for the product.", Toast.LENGTH_LONG).show();


            if(TextUtils.isEmpty(PName.getText().toString().trim()) && FilePathUri != null )
                Toast.makeText(getActivity(), "Please set a name for the product.", Toast.LENGTH_LONG).show();

            if (FilePathUri == null && !TextUtils.isEmpty(PName.getText().toString().trim()))
                Toast.makeText(getActivity(), "Please select image for the product.", Toast.LENGTH_LONG).show();

    }

}
