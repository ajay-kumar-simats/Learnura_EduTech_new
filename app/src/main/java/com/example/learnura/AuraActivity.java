package com.example.learnura;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AuraActivity extends AppCompatActivity {

    private Map<Integer, Runnable> menuActions;
    private static final int REQUEST_RECORD_AUDIO = 1;

    private FrameLayout sendQuery;
    private EditText queryET;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;

    private ChatFutures chatModel;
    private ChatDatabase chatDatabase;

   ImageView back;

    ImageView menu;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startSpeechRecognition();
                } else {
                    Toast.makeText(AuraActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aura);

        menuActions = new HashMap<>();
        menuActions.put(R.id.action_home, this::handleHomeAction);
        menuActions.put(R.id.action_profile, this::handleProfileAction);


        menu = findViewById(R.id.menu_icon);
        back = findViewById(R.id.back_aura);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuraActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(AuraActivity.this, menu);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                Runnable action = menuActions.get(item.getItemId());
                if (action != null) {
                    action.run();
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

        sendQuery = findViewById(R.id.layoutSend);
        queryET = findViewById(R.id.queryEditText);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList, this);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        chatModel = getChatModel();
        chatDatabase = ChatDatabase.getInstance(this);



        sendQuery.setOnClickListener(v -> {
            String query = queryET.getText().toString().trim();

            if (!query.isEmpty()) {
                queryET.setText("");
                String timestamp = getCurrentTimestamp();
                addMessageToChat(query, true, timestamp);
                saveMessageToDatabase(new ChatMessageEntity(query, true, timestamp));

                String predefinedResponse = getPredefinedResponse(query);
                if (predefinedResponse != null) {
                    addMessageToChat(predefinedResponse, false, timestamp);
                    saveMessageToDatabase(new ChatMessageEntity(predefinedResponse, false, timestamp));
                } else {
                    GeminiResp.getResponse(chatModel, query, new ResponseCallback() {
                        @Override
                        public void onResponse(String response) {
                            String currentTimestamp = getCurrentTimestamp();
                            addMessageToChat(response, false, currentTimestamp);
                            saveMessageToDatabase(new ChatMessageEntity(response, false, currentTimestamp));
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Toast.makeText(AuraActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }
        });

        queryET.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (queryET.getRight() - queryET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        startSpeechRecognition();
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                    }
                    return true;
                }
            }
            return false;
        });

        loadChatMessagesFromDatabase();

        // Swipe-to-delete functionality
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                ChatMessage removedMessage = chatMessageList.get(position);

                // Delete the message from the database
                deleteMessageFromDatabase(removedMessage, position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(chatRecyclerView);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#813FF1"));

        // Load profile image and set it for sent messages
        loadProfileImageForSentMessages();
    }

    private void handleHomeAction() {
        Intent intent = new Intent(AuraActivity.this, AuraActivity.class);
        startActivity(intent);
    }

    private void handleProfileAction() {
        Intent intent = new Intent(AuraActivity.this, ProfileActivity.class);
        startActivity(intent);
    }





    private ChatFutures getChatModel() {
        GeminiResp model = new GeminiResp();
        GenerativeModelFutures modelFutures = model.getModel();
        return modelFutures.startChat();
    }

    private void addMessageToChat(String message, boolean isSentByUser, String timestamp) {
        chatMessageList.add(new ChatMessage(message, isSentByUser, timestamp));
        chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
        chatRecyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
    }

    private void saveMessageToDatabase(ChatMessageEntity chatMessageEntity) {
        AsyncTask.execute(() -> chatDatabase.chatMessageDao().insert(chatMessageEntity));
    }

    private void loadChatMessagesFromDatabase() {
        AsyncTask.execute(() -> {
            List<ChatMessageEntity> chatMessages = chatDatabase.chatMessageDao().getAllChatMessages();
            runOnUiThread(() -> {
                for (ChatMessageEntity chatMessageEntity : chatMessages) {
                    chatMessageList.add(new ChatMessage(chatMessageEntity.message, chatMessageEntity.isSentByUser, chatMessageEntity.timestamp));
                }
                chatAdapter.notifyDataSetChanged();
            });
        });
    }

    private void deleteMessageFromDatabase(ChatMessage chatMessage, int position) {
        AsyncTask.execute(() -> {
            // Find the corresponding ChatMessageEntity and delete it from the database
            ChatMessageEntity entityToDelete = chatDatabase.chatMessageDao().findByMessage(chatMessage.getMessage(), chatMessage.getTimestamp());
            if (entityToDelete != null) {
                chatDatabase.chatMessageDao().delete(entityToDelete);
                runOnUiThread(() -> {
                    chatMessageList.remove(position);
                    chatAdapter.notifyItemRemoved(position);
                });
            }
        });
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        startActivityForResult(intent, REQUEST_RECORD_AUDIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RECORD_AUDIO && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                queryET.setText(spokenText);
            }
        }
    }

    private String getPredefinedResponse(String query) {
        String lowerCaseQuery = query.toLowerCase();
        String[][] questionsAndResponses = {
                {"what is your name", "who are you", "can you tell me your name", "your name", "who is this", "who am i talking to", "who's there", "may i know your name", "tell me your name","name"},
                {"My name is Aura AI."},
                {"who is your creator", "who made you", "who developed you", "who created you", "who is your developer", "who built you", "creator", "who's your creator", "who's your father", "who's your mother", "who developed u","who made u","who created u"},
                {"My creator's name is Mr. Ajay Kumar .D. He is a professional Android app developer and embedded systems developer, founder of Ajashia Technologies. You can visit the company website at [Ajashia Technologies](https://ajashiatechnologies.neocities.org)."}
        };

        for (int i = 0; i < questionsAndResponses.length - 1; i++) {
            for (String question : questionsAndResponses[i]) {
                if (lowerCaseQuery.contains(question.toLowerCase())) {
                    return questionsAndResponses[i + 1][0];
                }
            }
        }
        return null;
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileImageForSentMessages();
    }

    private void loadProfileImageForSentMessages() {
        SharedPreferences sharedPreferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        int profileImageResId = sharedPreferences.getInt("profile_image", R.drawable.round_image_background); // Default image if none saved

        chatAdapter.setProfileImageResId(profileImageResId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AuraActivity.this,MainActivity.class);
        startActivity(intent);
    }
}







