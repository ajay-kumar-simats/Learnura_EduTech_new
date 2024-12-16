package com.example.learnura;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final Context context;
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private int profileImageResId;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        profileImageResId = sharedPreferences.getInt("profile_image", R.drawable.round_image_background); // default image if none selected
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).isSentByUser()) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_container_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_container_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(chatMessage, profileImageResId);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void removeItem(int position) {
        chatMessages.remove(position);
        notifyItemRemoved(position);
    }

    public void setProfileImageResId(int profileImageResId) {
        this.profileImageResId = profileImageResId;
        notifyDataSetChanged();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView textMessage;
        private final TextView textDateTime;
        private final ImageView profileImageView;

        SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            profileImageView = itemView.findViewById(R.id.imageProfile);
        }

        void bind(ChatMessage chatMessage, int profileImageResId) {
            textMessage.setText(chatMessage.getMessage());
            textDateTime.setText(chatMessage.getTimestamp());
            profileImageView.setImageResource(profileImageResId);

            // Make links clickable
            Linkify.addLinks(textMessage, Linkify.ALL);
            textMessage.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView textMessage;
        private final TextView textDateTime;
        private final ImageView profileImageView;

        ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            profileImageView = itemView.findViewById(R.id.imageProfile);
        }

        void bind(ChatMessage chatMessage) {
            textMessage.setText(chatMessage.getMessage());
            textDateTime.setText(chatMessage.getTimestamp());
            profileImageView.setImageResource(R.drawable.round_image_background); // Set your static profile image resource

            // Make links clickable
            Linkify.addLinks(textMessage, Linkify.ALL);
            textMessage.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
