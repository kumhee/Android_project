package com.example.ex09;

import java.util.*;

import retrofit2.Call;
import retrofit2.http.*;

public interface RemoteService {
    public static final String BASE_URL = "http://192.168.0.194:8080";

    //로그인 호출
    @POST("/user/login")
    Call<Integer> login(@Body UserVO vo);

    @GET("/user/list")
    Call <List<UserVO>> list(
            @Query("page") int page,
            @Query("size") int size,
            @Query("word") String word);

    @GET("/user/read/{uid}")
    Call<UserVO> read(@Path("uid") String uid);
}
