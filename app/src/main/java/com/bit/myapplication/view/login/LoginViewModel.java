package com.bit.myapplication.view.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.bit.myapplication.BuildConfig;
import com.bit.myapplication.repo.local.AppSharedPreferences;
import com.bit.myapplication.repo.server.LoginRequestModel;
import com.bit.myapplication.repo.server.LoginResponseModel;
import com.bit.myapplication.network.NetworkClient;
import com.bit.myapplication.network.ServiceApi;
import com.bit.myapplication.utility.Constants;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<LoginResponseModel> loginResponseLiveData = new MutableLiveData();
    private MutableLiveData<Throwable> loginErrorLiveData = new MutableLiveData();

    private CompositeDisposable compositeDisposable;
    private ServiceApi serviceApi;
    private Application application;


    public LoginViewModel(@NonNull final Application application) {
        super(application);
        this.application = application;
        compositeDisposable = new CompositeDisposable();
        serviceApi = new NetworkClient().getRetrofitClientInstance(BuildConfig.HOST_URL).create(ServiceApi.class);

    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public void login(final String email, final String password) {
        serviceApi.loginService(new LoginRequestModel(email, password)).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<LoginResponseModel>() {
            @Override
            public void onSubscribe(final Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(final LoginResponseModel loginResponseModel) {
                AppSharedPreferences.getPreferenceReference(application).setStringValue(Constants.ACCESS_TOKEN, loginResponseModel.getToken());
                AppSharedPreferences.getPreferenceReference(application).setStringValue(Constants.EMAIL, email);
                AppSharedPreferences.getPreferenceReference(application).setStringValue(Constants.PASSWORD, password);
               loginResponseLiveData.postValue(loginResponseModel);
            }

            @Override
            public void onError(final Throwable e) {
                getLoginErrorLiveData().postValue(e);
            }
        });

    }



    public LiveData<LoginResponseModel> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    public MutableLiveData<Throwable> getLoginErrorLiveData() {
        return loginErrorLiveData;
    }
}
