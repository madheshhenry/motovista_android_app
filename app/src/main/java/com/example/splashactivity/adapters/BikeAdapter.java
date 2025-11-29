package com.example.splashactivity.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splashactivity.BikeDetailsActivity;
import com.example.splashactivity.R;
import com.example.splashactivity.models.BikeModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    Context context;
    List<BikeModel> list;

    String BASE_URL = "http://10.78.84.188/motovista_api/";

    public BikeAdapter(Context context, List<BikeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bike_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BikeModel bike = list.get(position);

        holder.txtBrandModel.setText(bike.getBrand() + " " + bike.getModel());
        holder.txtPrice.setText("₹ " + bike.getPrice());

        String imgUrl = BASE_URL + bike.getImg_top();
        Picasso.get()
                .load(imgUrl)
                .placeholder(R.drawable.bike_placeholder)
                .error(R.drawable.bike_placeholder)
                .into(holder.imgBike);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BikeDetailsActivity.class);
            intent.putExtra("bike", bike);   // ✔ correct key
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBike;
        TextView txtBrandModel, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBike = itemView.findViewById(R.id.imgBike);
            txtBrandModel = itemView.findViewById(R.id.txtBrandModel);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}
