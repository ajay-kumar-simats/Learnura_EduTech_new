package com.example.learnura;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppUpdatesAdapter appUpdatesAdapter;

    private List<AppUpdate> appUpdateList = new ArrayList<>();
    private ApiService apiService;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        recyclerView = view.findViewById(R.id.update_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        appUpdatesAdapter = new AppUpdatesAdapter(appUpdateList);
        recyclerView.setAdapter(appUpdatesAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        fetchAppUpdates();

        return view;
    }

    private void fetchAppUpdates() {
        Call<List<AppUpdate>> call = apiService.getAppUpdates();

        call.enqueue(new Callback<List<AppUpdate>>() {
            @Override
            public void onResponse(Call<List<AppUpdate>> call, Response<List<AppUpdate>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    appUpdateList.clear();
                    appUpdateList.addAll(response.body());
                    appUpdatesAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                } else {
                    try {
                        Log.d("",response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(getContext(), "Failed to load updates!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AppUpdate>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}