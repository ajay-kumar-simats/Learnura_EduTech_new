package com.example.learnura;

import java.util.List;

        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import okhttp3.ResponseBody;
        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.GET;
        import retrofit2.http.Headers;
        import retrofit2.http.Multipart;
        import retrofit2.http.POST;
        import retrofit2.http.Part;
        import retrofit2.http.Query;

public interface ApiService {

    @Multipart
    @POST("upload_course.php")
    Call<ResponseBody> uploadCourse(
            @Part MultipartBody.Part file,
            @Part("course_name") RequestBody courseName,
            @Part("uploaded_by") RequestBody uploadedBy,
            @Part("category") RequestBody category
    );


    @GET("get_updates.php")
    Call<List<AppUpdate>> getAppUpdates();

    @GET("fetch_courses.php")
    Call<List<Course1>> getCourses();

    @GET("fetch_challenges.php")
    Call<List<Challenge>> getChallenges(@Query("course_id") int courseId);

    @Headers("Content-Type: application/json")
    @POST("upload_challenge.php")
    Call<ResponseBody> uploadChallenge(@Body Challenge challenge);

    @GET("fetch_video_path.php")
    Call<ResponseBody> getVideoPath(@Query("course_id") int courseId);


    @POST("upload_update.php")
    Call<ResponseBody> uploadAppUpdate(RequestBody appNameBody, RequestBody versionNumberBody, MultipartBody.Part imageBody);

    @POST("/predict")
    Call<PredictionResponse> getPrediction(@Body PredictionRequest request);

}
