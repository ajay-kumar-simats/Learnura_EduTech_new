package com.example.learnura;

        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = " https://c036-2405-201-e009-3299-3d52-aef2-c8e8-fdbe.ngrok-free.app/learnura_api/";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

