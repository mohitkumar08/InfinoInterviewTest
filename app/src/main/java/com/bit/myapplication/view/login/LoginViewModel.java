package com.bit.myapplication.view.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.bit.myapplication.BuildConfig;
import com.bit.myapplication.model.server.LoginRequestModel;
import com.bit.myapplication.model.server.LoginResponseModel;
import com.bit.myapplication.network.NetworkClient;
import com.bit.myapplication.network.ServiceApi;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<LoginResponseModel> loginResponseLiveData = new MutableLiveData();
    private MutableLiveData<Throwable> loginErrorLiveData = new MutableLiveData();

    private CompositeDisposable compositeDisposable;
    private ServiceApi serviceApi;


    public LoginViewModel(@NonNull final Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        serviceApi = new NetworkClient().getRetrofitClientInstance(BuildConfig.HOST_URL).create(ServiceApi.class);

    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public void login(String email, String password) {

        serviceApi.loginService(new LoginRequestModel(email, password)).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<LoginResponseModel>() {
            @Override
            public void onSubscribe(final Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(final LoginResponseModel loginResponseModel) {
                getLoginResponseLiveData().postValue(loginResponseModel);
            }

            @Override
            public void onError(final Throwable e) {

            }
        });
    }

    public MutableLiveData<LoginResponseModel> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    public MutableLiveData<Throwable> getLoginErrorLiveData() {
        return loginErrorLiveData;
    }
}
