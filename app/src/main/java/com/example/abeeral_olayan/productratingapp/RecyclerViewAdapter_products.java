package com.example.abeeral_olayan.productratingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Leenah on 02/12/17.
 */

public class RecyclerViewAdapter_products extends RecyclerView.Adapter<RecyclerViewAdapter_products.ViewHolder> {


    Context context;
    List<ImageUploadInfo> productsList;

    public RecyclerViewAdapter_products(Context context, List<ImageUploadInfo> TempList) {
        this.productsList = TempList;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter_products.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_products, parent, false);

        RecyclerViewAdapter_products.ViewHolder viewHolder = new RecyclerViewAdapter_products.ViewHolder(view);

        return viewHolder;
    }

    @Override
    //  onBindViewHolder called by RecyclerView to display the data at the specified position
    public void onBindViewHolder(RecyclerViewAdapter_products.ViewHolder holder, int position) {
        final ImageUploadInfo UploadInfo = productsList.get(position);
        holder.ProductName.setText(UploadInfo.getImageName());
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.ProductImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context,ProductPage.class);
                in.putExtra("productName", UploadInfo.getImageName());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {

        return productsList.size();
    }

    // ViewHolder is used to speed up rendering of the recyclerView
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ProductImage;
        public TextView ProductName;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            ProductImage = (ImageView) itemView.findViewById(R.id.ProductImage);

            ProductName = (TextView) itemView.findViewById(R.id.ProductName);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
        }
    }
}
