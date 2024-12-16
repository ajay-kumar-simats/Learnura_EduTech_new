package com.example.learnura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public abstract class TeacherCourseAdapter extends RecyclerView.Adapter<TeacherCourseAdapter.TeacherCourseViewHolder> {

    private List<Teacher_Course> courseList;
    private Context context;


    // Constructor
    public TeacherCourseAdapter(List<Teacher_Course> courseList) {
        this.courseList = courseList;

    }

    @NonNull
    @Override
    public TeacherCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_course, parent, false);
        context = parent.getContext();
        return new TeacherCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherCourseViewHolder holder, int position) {
        Teacher_Course course = courseList.get(position);

        // Set course name
        holder.courseName.setText(course.getTitle());





    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public abstract void onDeleteClick(Teacher_Course course, int position);

    public class TeacherCourseViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView courseThumbnail;
        TextView courseName;


        public TeacherCourseViewHolder(View itemView) {
            super(itemView);
            courseThumbnail = itemView.findViewById(R.id.course_thumbnail);
            courseName = itemView.findViewById(R.id.course_name);
        }
    }


}
