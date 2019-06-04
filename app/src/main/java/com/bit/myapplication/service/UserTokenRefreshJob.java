package com.bit.myapplication.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.bit.myapplication.BuildConfig;
import com.bit.myapplication.repo.local.AppSharedPreferences;
import com.bit.myapplication.repo.server.LoginRequestModel;
import com.bit.myapplication.repo.server.LoginResponseModel;
import com.bit.myapplication.network.NetworkClient;
import com.bit.myapplication.network.ServiceApi;
import com.bit.myapplication.utility.Constants;

import androidx.work.Worker;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserTokenRefreshJob extends Worker {

    @NonNull
    @Override
    public Result doWork() {
        refreshToken();
        return Worker.Result.SUCCESS;
    }

    @Override
    public void onStopped(final boolean cancelled) {
        super.onStopped(cancelled);
    }


    private void refreshToken() {
        ServiceApi serviceApi = new NetworkClient().getRetrofitClientInstance(BuildConfig.HOST_URL).create(ServiceApi.class);
        String email = AppSharedPreferences.getPreferenceReference(getApplicationContext()).getStringValue(Constants.EMAIL);
        String password = AppSharedPreferences.getPreferenceReference(getApplicationContext()).getStringValue(Constants.PASSWORD);

        serviceApi.loginService(new LoginRequestModel(email, password)).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<LoginResponseModel>() {
            @Override
            public void onSubscribe(final Disposable d) {
            }

            @Override
            public void onSuccess(final LoginResponseModel loginResponseModel) {
                AppSharedPreferences.getPreferenceReference(getApplicationContext()).setStringValue(Constants.ACCESS_TOKEN, loginResponseModel.getToken());
            }

            @Override
            public void onError(final Throwable e) {

            }
        });

    }


}
