package com.example.abeeral_olayan.productratingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abeeral-olayan on 12/14/17.
 */

public class ProductInfoAdmin extends Fragment {
    String titel;
    private DatabaseReference databaseReference,DatabaseListComent;
    private TextView tname,tprice,tdescription,tcategory;
    private Button edit,delete;
    private ImageView image;
    private ListView listComments;
    private ArrayList<String> ListComment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        titel = getArguments().getString("PName1");
        return inflater.inflate(R.layout.product_info_admin, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(titel);


        tname=(TextView)view.findViewById(R.id.name);
        tdescription=(TextView)view.findViewById(R.id.desc);
        image=(ImageView) view.findViewById(R.id.PImag);
        edit=(Button) view.findViewById(R.id.editinf);
        delete=(Button) view.findViewById(R.id.deletP);
        listComments= (ListView) view.findViewById(R.id.listCommentA);

        ListComment = new ArrayList<String>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("PRDB").child(titel);
        ValueEventListener EventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ImageUploadInfo product = dataSnapshot.getValue(ImageUploadInfo.class);
                tname.setText(product.getImageName());
                tdescription.setText(product.getPdesc());
                Picasso.with(getActivity()).load(product.getImageURL()).into(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(EventListener);


        DatabaseListComent = FirebaseDatabase.getInstance().getReference().child("Comments").child(titel);
        final ValueEventListener eventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren()==null){
                    Toast.makeText(getActivity().getApplicationContext(),"No comments",Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getActivity(), AdminHome2.class));
                }
                try { for (DataSnapshot ds : dataSnapshot.getChildren() ) {
                    String comment= ds.getValue(String.class);
                    ListComment.add(comment);
                    //suggested.add(sd.getImageName());
                    final ArrayAdapter<String> array;
                    array = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, ListComment);
                    listComments.setAdapter(array);
                }} catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        DatabaseListComent.addListenerForSingleValueEvent(eventListener3);




        delete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                AlertDialog diaBox = AskOption();
                diaBox.show();

            } });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new EditProdAdmin();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("pro", titel);
                fr.setArguments(args);
                ft.replace(R.id.content_frame, fr);
                ft.commit();

            }
        });
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                //.setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("PRDB").child(titel);
                        db.removeValue();

                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Comments").child(titel);
                        db1.removeValue();

                        Fragment fr = new ListCategories();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        ft.replace(R.id.content_frame, fr);
                        ft.commit();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
