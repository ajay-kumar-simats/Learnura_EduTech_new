package com.example.learnura;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.concurrent.Executor;

public class GeminiResp {

    public static void getResponse(ChatFutures chatModel, String query, ResponseCallback callback) {

        // Embed system instructions directly into the user message
        String systemInstructions = "You are an AI assistant. Answer only questions related to studies, education, general knowledge, and similar topics. Avoid answering questions unrelated to these domains.";
        String combinedMessage = systemInstructions + "\n\nUser Query: " + query;

        // Create the user message
        Content.Builder userMessageBuilder = new Content.Builder();
        userMessageBuilder.setRole("user");
        userMessageBuilder.addText(combinedMessage);
        Content userMessage = userMessageBuilder.build();

        Executor executor = Runnable::run;

        // Send the user message
        ListenableFuture<GenerateContentResponse> response = chatModel.sendMessage(userMessage);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                callback.onResponse(resultText);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                callback.onError(throwable);
            }
        }, executor);
    }

    public GenerativeModelFutures getModel() {

        String apiKey = BuildConfig.apiKey;

        // Define a safety setting to block harmful content
        SafetySetting harassmentSafety = new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH);

        // Configure the generation settings
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.9f;
        configBuilder.topK = 16;
        configBuilder.topP = 0.1f;

        GenerationConfig generationConfig = configBuilder.build();

        // Create the GenerativeModel object
        GenerativeModel gm = new GenerativeModel(
                "gemini-1.5-flash", // Model ID
                apiKey,            // API key for authentication
                generationConfig,  // Generation configuration
                Collections.singletonList(harassmentSafety) // Safety settings
        );

        // Return a future-enabled model interface
        return GenerativeModelFutures.from(gm);
    }
}
