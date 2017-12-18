package com.example.abeeral_olayan.productratingapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import static android.app.Activity.RESULT_OK;


public class EditProdAdmin extends Fragment {


    public  static final String Database_Path="All_Image_Uploads_Database";//-----------()____________________
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase3;

    private DatabaseReference mDatabase4;
    private DatabaseReference mDatabase22;
    private TextView PName;
    private EditText PpriceTEXT;
    private EditText PdescTEXT;
    private Spinner Pcat;
    private ImageView imageView;
    private ArrayList<String> categories;
    private Button Upload_image;
    private DatabaseReference databaseReference;
    private FirebaseUser user;//add it
    private  ImageUploadInfo ob;//-----------()---------------
    private ImageUploadInfo ob2;
    private String TempImageName;
    private String titel;

    //private ImageView Pimage;

    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference ref;
    DatabaseReference mostafa;

    int Image_Request_Code = 7;
    ProgressDialog progressDialog;


    // Folder path for Firebase Storage.
    private String Storage_Path = "Products Images";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        titel = getArguments().getString("pro");
        return inflater.inflate(R.layout.activity_edit_prodect_info_n, container,false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Edit prodect page");

        //////////////////

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
            public void onCancelled(DatabaseError databaseError) {}
        };

        mDatabase2.addListenerForSingleValueEvent(eventListener);
        // End Spinner code
        //retreve from firebase
        //  Spinner
        PName=(TextView)view.findViewById(R.id.PName);
        PpriceTEXT=(EditText) view.findViewById(R.id.Pprice);
        PdescTEXT=(EditText) view.findViewById(R.id.Pdesc);
        Pcat = (Spinner) view.findViewById(R.id.spinner);
        Upload_image = (Button) view.findViewById(R.id.Pimage);
        imageView=(ImageView) view.findViewById(R.id.imageView5);


        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("PRDB").child(titel);//-------_()---------
      //  mDatabase22=FirebaseDatabase.getInstance().getReference().child("PRDB");//---------()---
            /* try{
            ValueEventListener EventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String pname = dataSnapshot.getValue(String.class);
                    PName.setText(pname);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabase3.addListenerForSingleValueEvent(EventListener);
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }*/
        //databaseReference=mDatabase2.child(PNameTEXT.getText().toString());
        //PNameTEXT.setText(databaseReference.getText().toString());

        user = FirebaseAuth.getInstance().getCurrentUser();
        try{
            mDatabase1 = FirebaseDatabase.getInstance().getReference();
            ref =  mDatabase1.child("PRDB").child(titel);////////////////change accroding to prodect page


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
                        Picasso.with(getContext()).load(ob.getImageURL()).placeholder(R.mipmap.ic_launcher).into(imageView);// add now----------
                        //if(ob.getImageURL()!=null)
                        //FilePathUri= Uri.parse(ob.getImageURL());
                        //Upload_image.setPressed(true);
                        //if(ob.getImageName()!=null)
                        PName.setText(ob.getImageName());
                    }catch (Exception e){Toast.makeText(getActivity(),"line178",Toast.LENGTH_LONG).show();}

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            ref.addListenerForSingleValueEvent(EventListener2);
        }catch (Exception e){
            Toast.makeText(getActivity(), "lin186", Toast.LENGTH_LONG).show();
        }

        ////////////////////
        // Start Image code
        storageReference = FirebaseStorage.getInstance().getReference();
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("PRDB").child("PInfo");

        //////////////
        progressDialog = new ProgressDialog(getActivity());
        // Adding click listener to Choose image button.
        try{
            Upload_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();

                    // Setting intent type as image to select image from phone storage.
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                }
            });}catch (Exception e){
            Toast.makeText(getActivity(), "line210", Toast.LENGTH_LONG).show();
        }
        /////////////////////



        //to create listner, update info
        try {
            view.findViewById(R.id.AddPB).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditProdectInfo();
                    Fragment fr = new ListCategories();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle args = new Bundle();
                    args.putString("PName1", titel);
                    fr.setArguments(args);
                    ft.replace(R.id.content_frame, fr);
                    ft.commit();
                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            view.findViewById(R.id.CanclePB).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fr = new ListCategories();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Bundle args = new Bundle();
                    args.putString("PName1", titel);
                    fr.setArguments(args);
                    ft.replace(R.id.content_frame, fr);
                    ft.commit();
                }
            });

        }catch (Exception e){
            Toast.makeText(getActivity(), "line237", Toast.LENGTH_LONG).show();}


    }//oncreat



    //retrev sppiner by value/////////////////////
    private int getIndex(Spinner pcat, String pcat1) {
        int index = 0;

        for (int i=0;i<pcat.getCount();i++){
            if (pcat.getItemAtPosition(i).equals(pcat1)){
                index = i;
            }
        }
        return index;
    }
    //////////////////////////////
    /* altrentove way of get sppiner value
    If you are using an ArrayList for your Spinner Adapter then you can use that to loop thru and get the index. Another way is is to loop thru the adapter entries
Spinner s = (Spinner) findViewById(R.id.spinner_id);
for(i=0; i < adapter.getCount(); i++) {
  if(myString.trim().equals(adapter.getItem(i).toString())){
    s.setSelection(i);
    break;
  }
}*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if(FilePathUri==null)
            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                //Setting up bitmap selected image into ImageView.------------()___________--------------
                imageView.setImageBitmap(bitmap);

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
    public void EditProdectInfo() {
        if (FilePathUri == null && !TextUtils.isEmpty(PName.getText().toString().trim())) {
            // progressDialog.setTitle("Product is Editing...");
            // Getting image name from EditText and store into string variable.
            TempImageName = PName.getText().toString().trim();
            String Pdesc1 = PdescTEXT.getText().toString().trim();
            String Pprice1 = PpriceTEXT.getText().toString().trim();
            String Pcat1 = Pcat.getSelectedItem().toString().trim();



            //@SuppressWarnings("VisibleForTests")///////////////////8*******************
            ////ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString() ,Pdesc1,Pprice1,Pcat1,"","");
            //ob = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString() ,Pdesc1,Pprice1,Pcat1,"","");

            ob.setImageName(TempImageName);
            ob.setImageURL(ob.getImageURL());
            ob.setPcat(Pcat1);
            ob.setPdesc(Pdesc1);
            ob.setPprice(Pprice1);

            // Hiding the progressDialog after done uploading.
            //  progressDialog.dismiss();

            // Showing toast message after done uploading.
            ///////////////////
            Toast.makeText(getActivity(), "Product Editeing Successfully ", Toast.LENGTH_LONG).show();

            //ob2=ob;
            // Getting image upload ID.
            //String ImageUploadId = databaseReference.push().getKey();

            // Adding image upload id s child element into databaseReference.
            //databaseReference.child(PName.getText().toString()).setValue(ob);
            mDatabase3.removeValue();
            //mDatabase4 =mDatabase3.push();
            //mDatabase22.child(TempImageName).setValue(ob);
            mDatabase3.setValue(ob);
            //ob=ob2;
            //ref.setValue(ob);
            if (TextUtils.isEmpty(PName.getText().toString().trim()))
                Toast.makeText(getActivity(), "Please set a name for the product.", Toast.LENGTH_LONG).show();
        }else {
            try {
                // Checking whether FilePathUri Is empty or not.
                if (FilePathUri != null && !TextUtils.isEmpty(PName.getText().toString().trim())) {

                    // Setting progressDialog Title.
                    // progressDialog.setTitle("Product is Editing...");

                    // Showing progressDialog.
                    // progressDialog.show();
                    // Creating second StorageReference.
                    StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                    // Adding addOnSuccessListener to second StorageReference.
                    //if(Upload_image.performClick()==false)
                    //storageReference2nd.cancel();


                    storageReference2nd.putFile(FilePathUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    // Getting image name from EditText and store into string variable.
                                    TempImageName = PName.getText().toString().trim();
                                    String Pdesc1 = PdescTEXT.getText().toString().trim();
                                    String Pprice1 = PpriceTEXT.getText().toString().trim();
                                    String Pcat1 = Pcat.getSelectedItem().toString().trim();


                                    // Hiding the progressDialog after done uploading.
                                    // progressDialog.dismiss();

                                    // Showing toast message after done uploading.
                                    ///////////////////
                                    Toast.makeText(getActivity(), "Product Editeing Successfully ", Toast.LENGTH_LONG).show();

                                    //@SuppressWarnings("VisibleForTests")///////////////////8*******************
                                    ////ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString() ,Pdesc1,Pprice1,Pcat1,"","");
                                    //ob = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString() ,Pdesc1,Pprice1,Pcat1,"","");

                                    ob.setImageName(TempImageName);
                                    ob.setImageURL(taskSnapshot.getDownloadUrl().toString());
                                    ob.setPcat(Pcat1);
                                    ob.setPdesc(Pdesc1);
                                    ob.setPprice(Pprice1);


                                    //ob2=ob;
                                    // Getting image upload ID.
                                    //String ImageUploadId = databaseReference.push().getKey();

                                    // Adding image upload id s child element into databaseReference.
                                    //databaseReference.child(PName.getText().toString()).setValue(ob);
                                    mDatabase3.removeValue();
                                    //mDatabase4 =mDatabase3.push();
                                    mDatabase3.setValue(ob);
                                    //mDatabase22.child(TempImageName).setValue(ob);
                                    //ob=ob2;
                                    //ref.setValue(ob);
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
                                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })

                            // On progress change upload time.
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    // Setting progressDialog Title.
                                    // progressDialog.setTitle("Product is Editing...");//------------()----------------

                                }
                            });
                }


                //if (TextUtils.isEmpty(PName.getText().toString().trim()) && FilePathUri == null)
                //Toast.makeText(this, "Please set a name and select image for the product.", Toast.LENGTH_LONG).show();


               // if (TextUtils.isEmpty(PName.getText().toString().trim()))
                  //  Toast.makeText(getActivity(), "Please set a name for the product.", Toast.LENGTH_LONG).show();

                // if (FilePathUri == null && !TextUtils.isEmpty(PName.getText().toString().trim()))
                // Toast.makeText(EditProdectInfoN.this, "Please select image for the product.", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }//else

    }//edir




}


