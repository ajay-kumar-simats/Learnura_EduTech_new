package com.example.learnura;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class InviteFriendFragment extends Fragment {

    private static final String SAMPLE_INVITE_LINK = "https://learnura_edutech.com/invite";

    public InviteFriendFragment() {
        // Required empty public constructor
    }

    public static InviteFriendFragment newInstance(String param1, String param2) {
        InviteFriendFragment fragment = new InviteFriendFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_friend, container, false);

        // Change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#813FF1")); // Your desired color
        }

        // Initialize UI elements
        EditText descriptionEditText = view.findViewById(R.id.editTextText6);
        Button sendInvitationButton = view.findViewById(R.id.button4);
        ImageView copyLinkImageView = view.findViewById(R.id.imageView19);

        // Set up the "Copy Link" functionality
        copyLinkImageView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Invite Link", SAMPLE_INVITE_LINK);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(requireContext(), "Link copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        // Set up the "Send Invitation Link" button
        sendInvitationButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString().trim();
            if (description.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a description.", Toast.LENGTH_SHORT).show();
                return;
            }

            String shareContent = description + "\n\nInvite Link: " + SAMPLE_INVITE_LINK;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);

            startActivity(Intent.createChooser(shareIntent, "Share Invitation Via"));
        });

        return view;
    }
}
