package com.example.learnura;

public interface ResponseCallback {

    void onResponse(String response);
    void onError(Throwable throwable);

    void onError(String error);
}
