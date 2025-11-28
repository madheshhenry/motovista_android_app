package com.example.splashactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.splashactivity.R;
import com.example.splashactivity.models.BikeModel;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    Context context;
    List<BikeModel> list;
    OnBikeClickListener listener;

    public interface OnBikeClickListener {
        void onBikeClick(BikeModel bike);
    }

    public BikeAdapter(Context context, List<BikeModel> list, OnBikeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_bike_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BikeModel bike = list.get(position);

        holder.tvBrand.setText(bike.getBrand());
        holder.tvModel.setText(bike.getModel());
        holder.tvPrice.setText("₹ " + bike.getPrice());

        Glide.with(context).load(bike.getImage()).into(holder.imgBike);

        holder.itemView.setOnClickListener(v -> listener.onBikeClick(bike));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBike;
        TextView tvBrand, tvModel, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBike = itemView.findViewById(R.id.imgBike);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
