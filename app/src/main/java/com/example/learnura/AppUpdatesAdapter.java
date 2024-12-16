package com.example.learnura;



import static com.example.learnura.ApiClient.BASE_URL;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AppUpdatesAdapter extends RecyclerView.Adapter<AppUpdatesAdapter.ViewHolder> {

    private final List<AppUpdate> appUpdateList;

    // Constructor to pass the list of updates
    public AppUpdatesAdapter(List<AppUpdate> appUpdateList) {
        this.appUpdateList = appUpdateList;
    }

    // ViewHolder class to hold references to the views in each RecyclerView item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView updateNameTextView;
        TextView updateVersionTextView;
        ImageView updateImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            updateNameTextView = itemView.findViewById(R.id.update_name_txt);
            updateVersionTextView = itemView.findViewById(R.id.update_version_txt);
            updateImageView = itemView.findViewById(R.id.ivAppUpdateImg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.update_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current update item
        AppUpdate appUpdate = appUpdateList.get(position);

        // Bind data to the TextViews
        holder.updateNameTextView.setText(appUpdate.getAppName()); // Changed to match the new model field
        holder.updateVersionTextView.setText("Version: " + appUpdate.getVersion()); // Label added for better readability


        Glide.with(holder.updateImageView.getContext())
                .load(BASE_URL+appUpdate.getImageUrl())
                .placeholder(R.drawable.avatar1)
                .error(R.drawable.ic_delete)
                .into(holder.updateImageView);
    }


    @Override
    public int getItemCount() {
        return appUpdateList.size();
    }
}
