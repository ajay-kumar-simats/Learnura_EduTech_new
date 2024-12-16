package com.example.learnura;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CourseApi {
    @Multipart
    @POST("upload_course.php")
    Call<ResponseBody> uploadCourse(
            @Part MultipartBody.Part file,
            @Part("course_name") RequestBody courseName,
            @Part("uploaded_by") RequestBody uploadedBy,
            @Part("category") RequestBody category  // Added category
    );

    @GET("fetch_all_courses.php")
    Call<CourseResponse> getCourses();
}
