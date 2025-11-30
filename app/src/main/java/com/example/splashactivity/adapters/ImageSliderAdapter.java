package com.example.splashactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splashactivity.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder> {

    private final Context context;
    private final List<String> images;

    public ImageSliderAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load(images.get(position))
                .placeholder(R.drawable.bike_placeholder)
                .error(R.drawable.bike_placeholder)
                .into(holder.imgSlider);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSlider;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlider = itemView.findViewById(R.id.imgSlider);
        }
    }
}
