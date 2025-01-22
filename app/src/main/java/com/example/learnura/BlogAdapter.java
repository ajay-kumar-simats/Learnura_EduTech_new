package com.example.learnura;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private ArrayList<Blog> blogList;

    public BlogAdapter(ArrayList<Blog> blogList) {
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.titleTextView.setText(blog.getTitle());
        holder.detailTextView.setText(blog.getDetail());
        holder.descriptionTextView.setText(blog.getDescription());
        holder.linkTextView.setText(blog.getLink());

        // Set the link as clickable
        holder.linkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(blog.getLink()));
            holder.linkTextView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, detailTextView, descriptionTextView, linkTextView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.blogTitleTextView);
            detailTextView = itemView.findViewById(R.id.blogDetailTextView);
            descriptionTextView = itemView.findViewById(R.id.blogDescriptionTextView);
            linkTextView = itemView.findViewById(R.id.blogLinkTextView);
        }
    }
}
