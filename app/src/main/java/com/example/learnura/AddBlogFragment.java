package com.example.learnura;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddBlogFragment extends Fragment {

    private EditText blogTitleEditText, blogDetailEditText, blogLinkEditText, blogDescriptionEditText;
    private AppCompatButton updateBlogButton;

    private static final String ADD_BLOG_URL = "https://7d3a-2405-201-e009-3299-6912-5674-dc40-a7d8.ngrok-free.app/learnura_api/add_blog.php";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_blog, container, false);

        blogTitleEditText = view.findViewById(R.id.blog_title_editText);
        blogDetailEditText = view.findViewById(R.id.editText5);
        blogLinkEditText = view.findViewById(R.id.editText6);
        blogDescriptionEditText = view.findViewById(R.id.cardView_choose_image);
        updateBlogButton = view.findViewById(R.id.update_blog_button);

        updateBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBlog();
            }
        });

        return view;
    }

    private void addBlog() {
        String title = blogTitleEditText.getText().toString().trim();
        String detail = blogDetailEditText.getText().toString().trim();
        String link = blogLinkEditText.getText().toString().trim();
        String description = blogDescriptionEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(detail) || TextUtils.isEmpty(link) || TextUtils.isEmpty(description)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_BLOG_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Failed to add blog: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("detail", detail);
                params.put("link", link);
                params.put("description", description);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
