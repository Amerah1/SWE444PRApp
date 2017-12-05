package com.example.abeeral_olayan.productratingapp;

/**
 * Created by Leenah on 28/11/17.
 */
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<ImageUpload_Category> categoriesList;

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
        final ImageUpload_Category UploadInfo = categoriesList.get(position);
        holder.CategoryName.setText(UploadInfo.getImageName());
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.CategoryImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context,ProductsView.class);
                in.putExtra("CatName", UploadInfo.getImageName());
                context.startActivity(in);
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