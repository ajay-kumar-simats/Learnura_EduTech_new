package com.example.learnura;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InternetAdapter extends RecyclerView.Adapter<InternetAdapter.InternetViewHolder> {

    private List<Course1> internetCoursesList;
    private List<Course1> fullItemList;
    private Context context;

    public InternetAdapter(List<Course1> internetCoursesList, Context context) {
        this.internetCoursesList = new ArrayList<>();
        this.fullItemList = new ArrayList<>();
        this.internetCoursesList.addAll(internetCoursesList);
        this.fullItemList.addAll(this.internetCoursesList);
        this.context = context;
    }

    @NonNull
    @Override
    public InternetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arduino_category_layout, parent, false);
        return new InternetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InternetViewHolder holder, int position) {
        Course1 currentItem = internetCoursesList.get(position);

        holder.mainTitle.setText(currentItem.getCourseName());
        holder.subTitle.setText(currentItem.getUploadedBy());
        holder.imageThumbnail.setImageResource(R.drawable.arduino); // Set your placeholder image

        holder.start.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("course_id", currentItem.getCourseId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return internetCoursesList.size();
    }

    public void filter(String internet_text) {
        internetCoursesList.clear();
        if (internet_text.isEmpty()){
            internetCoursesList.addAll(fullItemList);
        }else{
            internet_text = internet_text.toLowerCase();
            for (Course1 internet : fullItemList){
                if(internet.getCourseName().toLowerCase().contains(internet_text)){
                    internetCoursesList.add(internet);
                }
            }
        }
        notifyDataSetChanged();

    }


    public static class InternetViewHolder extends RecyclerView.ViewHolder {

        private TextView mainTitle;
        private TextView subTitle;
        private ImageView imageThumbnail;
        private Button start;

        public InternetViewHolder(@NonNull View itemView) {
            super(itemView);
            mainTitle = itemView.findViewById(R.id.main_course_title);
            subTitle = itemView.findViewById(R.id.sub_title);
            imageThumbnail = itemView.findViewById(R.id.arduino_thumnail);
            start = itemView.findViewById(R.id.start);
        }
    }
}
