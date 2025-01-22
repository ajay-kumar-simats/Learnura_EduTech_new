package com.example.learnura;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView blogRecyclerView;
    private BlogAdapter blogAdapter;
    private ArrayList<Blog> blogList;

    private static final String FETCH_BLOGS_URL = "https://c4ac-2405-201-e009-3299-2c4e-62b-1ccd-2a36.ngrok-free.app/learnura_api/fetch_blogs.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        blogRecyclerView = findViewById(R.id.blogRecyclerView);
        blogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        blogList = new ArrayList<>();
        blogAdapter = new BlogAdapter(blogList);
        blogRecyclerView.setAdapter(blogAdapter);

        fetchBlogs();
    }

    private void fetchBlogs() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, FETCH_BLOGS_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject blogObject = response.getJSONObject(i);
                                String title = blogObject.getString("title");
                                String detail = blogObject.getString("detail");
                                String link = blogObject.getString("link");
                                String description = blogObject.getString("description");

                                blogList.add(new Blog(title, detail, link, description));
                            }
                            blogAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BlogActivity.this, "Failed to parse blogs", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BlogActivity.this, "Failed to fetch blogs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }
}
