package com.example.learnura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerViewCourseListAdapter extends RecyclerView.Adapter<RecyclerViewCourseListAdapter.MyViewHolder> {

    private final Context context;
    private final List<Course> courseList;

    // Updated constructor to accept a Fragment instead of FragmentActivity
    public RecyclerViewCourseListAdapter(Fragment fragment, List<Course> courseList) {
        this.context = fragment.getContext();  // Get the fragment context
        this.courseList = courseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_course_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvCourseTitle.setText(courseList.get(position).getTitle());
        holder.ivCourseImg.setImageResource(courseList.get(position).getImage());

        holder.cardView.setOnClickListener(v ->
                Toast.makeText(context, courseList.get(position).getTitle(), Toast.LENGTH_LONG).show()
        );
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCourseTitle;
        public ImageView ivCourseImg;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCourseTitle = itemView.findViewById(R.id.tvCourseTitle);
            ivCourseImg = itemView.findViewById(R.id.ivCourseImg);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
