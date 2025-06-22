package com.example.LibraryApp.Connection;

import com.example.LibraryApp.Auth.UserCredential;
import com.example.LibraryApp.Screens.Attendance.AttendanceModel;
import com.example.LibraryApp.Screens.Home.LectureModel;
import com.example.LibraryApp.Screens.Profile.ServerResponse;
import com.example.LibraryApp.Screens.Profile.UserModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    //for user auth

    @FormUrlEncoded
    @POST("API/User/Auth.php")
    Call<UserCredential> auth(@FieldMap Map<String,String> fields);


    //get user data to show in profile
    @FormUrlEncoded
    @POST("API/User/getUserData.php")
    Call<UserModel> getProfileDetails(@FieldMap Map<String,String> fields);


    @FormUrlEncoded
    @POST("API/User/userImageUpload.php")
    Call<ServerResponse> updateProfile(@FieldMap Map<String,String> fields);



    //for get all available Lectures
    @FormUrlEncoded
    @POST("API/User/getLectures.php")
    Call<List<LectureModel>> getLectures(@FieldMap Map<String,String> fields);


    //Mark Attendance
    @FormUrlEncoded
    @POST("API/User/markAttendance.php")
    Call<ServerResponse> markAttendance(@FieldMap Map<String,String> fields);


    //for get all marked Attendance
    @FormUrlEncoded
    @POST("API/User/getAttendance.php")
    Call<List<AttendanceModel>> getAttendance(@FieldMap Map<String,String> fields);

}
