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

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder> {

    private List<Course1> programmingCoursesList;

    private List<Course1> fullItemList;
    private Context context;

    public ProgrammingAdapter(List<Course1> programmingCoursesList, Context context) {
        this.programmingCoursesList = new ArrayList<>();
        this.fullItemList = new ArrayList<>();
        this.programmingCoursesList.addAll(programmingCoursesList);
        this.fullItemList.addAll(this.programmingCoursesList);
        this.context = context;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.programming_category_layout, parent, false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {
        Course1 currentItem = programmingCoursesList.get(position);

        holder.mainTitle.setText(currentItem.getCourseName());
        holder.subTitle.setText(currentItem.getUploadedBy());
        holder.imageThumbnail.setImageResource(R.drawable.arduino); // Set your placeholder image

        holder.start_p.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("course_id", currentItem.getCourseId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return programmingCoursesList.size();
    }

    public void filter(String programming_text) {
        programmingCoursesList.clear();
        if (programming_text.isEmpty()){
            programmingCoursesList.addAll(fullItemList);
        }else{
            programming_text = programming_text.toLowerCase();
            for (Course1 programming : fullItemList){
                if (programming.getCourseName().toLowerCase().contains(programming_text)){
                    programmingCoursesList.add(programming);
                }
            }
        }
        notifyDataSetChanged();
    }


    public static class ProgrammingViewHolder extends RecyclerView.ViewHolder {

        private TextView mainTitle;
        private TextView subTitle;
        private ImageView imageThumbnail;
        private Button start_p;

        public ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);
            mainTitle = itemView.findViewById(R.id.main_course_title);
            subTitle = itemView.findViewById(R.id.sub_title);
            imageThumbnail = itemView.findViewById(R.id.arduino_thumnail);
            start_p = itemView.findViewById(R.id.start_p);
        }
    }
}
