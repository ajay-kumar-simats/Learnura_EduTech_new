package com.example.learnura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArduinoAdapter extends RecyclerView.Adapter<ArduinoAdapter.ArduinoViewHolder> {

    private List<Course1> arduinoCoursesList;

    private List<Course1> fullItemList;
    private Context context;

    public ArduinoAdapter(List<Course1> arduinoCoursesList, Context context) {
        this.arduinoCoursesList = new ArrayList<>();
        this.fullItemList = new ArrayList<>();
        this.arduinoCoursesList.addAll(arduinoCoursesList);
        this.fullItemList.addAll(this.arduinoCoursesList);
        this.context = context;
    }

    @NonNull
    @Override
    public ArduinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arduino_category_layout, parent, false);
        return new ArduinoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArduinoViewHolder holder, int position) {
        Course1 currentItem = arduinoCoursesList.get(position);

        holder.mainTitle.setText(currentItem.getCourseName());
        holder.subTitle.setText(currentItem.getUploadedBy());
        holder.imageThumbnail.setImageResource(R.drawable.arduino); // Set your placeholder image

        holder.start.setOnClickListener(v -> {


                // Proceed to the course
                String baseUrl = "https://7d3a-2405-201-e009-3299-6912-5674-dc40-a7d8.ngrok-free.app/learnura_api/";
                String videoPath = currentItem.getFilePath(); // e.g., "uploads_videos/your_video.mp4"
                String videoUrl = baseUrl + videoPath;

                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("course_id", currentItem.getCourseId());
                intent.putExtra("course_name", currentItem.getCourseName());
                intent.putExtra("video_path", videoUrl);
                intent.putExtra("challenges", (Serializable) currentItem.getChallenges());
                context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return arduinoCoursesList.size();
    }

    public void filter(String text) {
        arduinoCoursesList.clear();
        if (text.isEmpty()){
            arduinoCoursesList.addAll(fullItemList);
        }else{
            text = text.toLowerCase();
            for(Course1 arduino : fullItemList){
                if (arduino.getCourseName().toLowerCase().contains(text)){
                    arduinoCoursesList.add(arduino);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ArduinoViewHolder extends RecyclerView.ViewHolder {

        private final TextView mainTitle;
        private final TextView subTitle;
        private final ImageView imageThumbnail;
        private final Button start;

        public ArduinoViewHolder(@NonNull View itemView) {
            super(itemView);
            mainTitle = itemView.findViewById(R.id.main_course_title);
            subTitle = itemView.findViewById(R.id.sub_title);
            imageThumbnail = itemView.findViewById(R.id.arduino_thumnail);
            start = itemView.findViewById(R.id.start);
        }
    }







}
