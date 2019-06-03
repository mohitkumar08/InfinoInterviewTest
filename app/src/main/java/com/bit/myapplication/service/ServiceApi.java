package com.bit.myapplication.service;


import com.bit.myapplication.model.server.LoginRequestModel;
import com.bit.myapplication.model.server.LoginResponseModel;
import io.reactivex.Single;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("login")
    Single<LoginResponseModel> loginService(LoginRequestModel loginRequestModel);


}