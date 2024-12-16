package com.example.learnura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
    private Context context;
    private List<Avatar> avatars;
    private OnAvatarClickListener listener;

    public interface OnAvatarClickListener {
        void onAvatarClick(Avatar avatar);
    }

    public AvatarAdapter(Context context, List<Avatar> avatars, OnAvatarClickListener listener) {
        this.context = context;
        this.avatars = avatars;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_avatar, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        Avatar avatar = avatars.get(position);
        holder.ivAvatar.setImageResource(avatar.getImageResId());
        holder.tvAvatarName.setText(avatar.getName());
        holder.itemView.setOnClickListener(v -> listener.onAvatarClick(avatar));
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    public static class AvatarViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvAvatarName;

        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvAvatarName = itemView.findViewById(R.id.tv_avatar_name);
        }
    }
}
