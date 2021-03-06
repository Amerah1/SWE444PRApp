package com.example.abeeral_olayan.productratingapp;

/**
 * Created by Leenah on 28/11/17.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    private Activity contexe1;
    List<ImageUpload_Category> categoriesList;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    public RecyclerViewAdapter(Context context, List<ImageUpload_Category> TempList) {
        this.categoriesList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    //  onBindViewHolder called by RecyclerView to display the data at the specified position
    public void onBindViewHolder(ViewHolder holder, int position) {

        firebaseAuth = FirebaseAuth.getInstance();

        final ImageUpload_Category UploadInfo = categoriesList.get(position);
        holder.CategoryName.setText(UploadInfo.getImageName());
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.CategoryImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id= user.getUid();
                if(id.equals("aSK7RyMA8xfdaQNPF0xS6kAumam2")){
                    Fragment fragment = new ProductListAdmin();
                    Bundle args = new Bundle();
                    args.putString("CName", UploadInfo.getImageName());
                    fragment.setArguments(args);



                    //getFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();


                        //Inflate the fragment
                    //contexe1.getFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();

                }else{
                    Intent in = new Intent(context,ProductsView.class);
                    in.putExtra("CatName", UploadInfo.getImageName());
                    context.startActivity(in);
                }

            }
        });
    }

    @Override
    public int getItemCount() {

        return categoriesList.size();
    }

    // ViewHolder is used to speed up rendering of the recyclerView
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView CategoryImage;
        public TextView CategoryName;
        public CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            CategoryImage = (ImageView) itemView.findViewById(R.id.CategoryImage);

            CategoryName = (TextView) itemView.findViewById(R.id.CategoryName);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);

        }
    }
}