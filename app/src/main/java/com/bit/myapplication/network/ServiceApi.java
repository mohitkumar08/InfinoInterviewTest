package com.bit.myapplication.network;


import com.bit.myapplication.repo.server.LoginRequestModel;
import com.bit.myapplication.repo.server.LoginResponseModel;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("login")
    Single<LoginResponseModel> loginService(@Body LoginRequestModel loginRequestModel);

}