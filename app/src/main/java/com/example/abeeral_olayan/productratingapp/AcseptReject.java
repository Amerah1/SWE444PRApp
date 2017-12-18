package com.example.abeeral_olayan.productratingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by abeeral-olayan on 11/18/17.
 */

public class AcseptReject extends Fragment {

    private DatabaseReference databaseReference , data, data1;
    private TextView tname,tprice,tdescription,tcategory;
    private Button approv,reject;
    private ImageView image;
    private String name,price,desc,cat,url, titel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        titel = getArguments().getString("PName");
        return inflater.inflate(R.layout.activity_acsept_reject, container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(titel);
        //Bundle bundle= getActivity().getIntent().getExtras();


        //if (bundle!=null) {
        //  titel=getArguments().getString("PName");
        // getActivity().setTitle(titel);}
        //getActivity().setTitle("Product information");}
        tname=(TextView)view.findViewById(R.id.textname);
        tprice=(TextView)view.findViewById(R.id.textPrice);
        tcategory=(TextView)view.findViewById(R.id.textCategory);
        tdescription=(TextView)view.findViewById(R.id.textDescription);
        image=(ImageView) view.findViewById(R.id.PImage);
        approv=(Button) view.findViewById(R.id.ApproveB);
        reject=(Button) view.findViewById(R.id.RejectB);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SPRDB").child(titel);
                db.removeValue();
                try {

                    data1=FirebaseDatabase.getInstance().getReference().child("SPRDB");
                    ValueEventListener EventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                String num= String.valueOf(dataSnapshot.getChildrenCount());
                            int size=Integer.parseInt(num.substring(num.length()-1));

                                //AdminHome2 adminHome2 = new AdminHome2();
                                //adminHome2.initializeCountDrawer();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    data1.addListenerForSingleValueEvent(EventListener);



                    Toast.makeText(getActivity(), "Rejected Successfully", Toast.LENGTH_LONG).show();
                    Fragment fr = new SuggestedProducts();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, fr);
                    ft.commit();  } catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("SPRDB").child(titel);
        ValueEventListener EventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SuggestProducctInfo product = dataSnapshot.getValue(SuggestProducctInfo.class);
                name=product.getSPName();
                tname.setText(name);
                price=product.getSPprice();
                tprice.setText(price);
                cat=product.getSPcat();
                tcategory.setText(cat);
                desc=product.getSPdesc();
                tdescription.setText(desc);
                url=product.getSPimageURL();
                Picasso.with(getActivity()).load(url).into(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(EventListener);


        approv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUploadInfo pr=new ImageUploadInfo(name,url,desc,price,cat,"0","0");
                FirebaseDatabase.getInstance().getReference().child("PRDB").child(titel).setValue(pr);
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SPRDB").child(titel);
                db.removeValue();

                try {
                    data1=FirebaseDatabase.getInstance().getReference().child("SPRDB");
                    ValueEventListener EventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                String num= String.valueOf(dataSnapshot.getChildrenCount());
                                int size=Integer.parseInt(num.substring(num.length()-1));
                                //AdminHome2 adminHome2 = new AdminHome2();
                               // adminHome2.notifyProductSuggested(size);

                            }catch (Exception e){
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();}
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    data1.addListenerForSingleValueEvent(EventListener);


                    Toast.makeText(getActivity(), "Accepted Successfully", Toast.LENGTH_LONG).show();
                    Fragment fr = new SuggestedProducts();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.content_frame, fr);
                    ft.commit();

                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //}
    }
}

