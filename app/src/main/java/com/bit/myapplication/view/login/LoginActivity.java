package com.bit.myapplication.view.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.bit.myapplication.R;
import com.bit.myapplication.repo.local.AppSharedPreferences;
import com.bit.myapplication.repo.server.LoginResponseModel;
import com.bit.myapplication.service.UserTokenRefreshJob;
import com.bit.myapplication.utility.Constants;
import com.bit.myapplication.view.detail.DetailActivity;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppSharedPreferences.getPreferenceReference(getApplicationContext()).getStringValue(Constants.ACCESS_TOKEN) != null) {
            startActivity(new Intent(this, DetailActivity.class));
            this.finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this, new AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mEmailView.setText(Constants.USER_NAME);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setText(Constants.USER_PASSWORD);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setVisibility(View.VISIBLE);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        addObserver();
    }

    private void addObserver() {
        loginViewModel.getLoginResponseLiveData().observe(this, new Observer<LoginResponseModel>() {
            @Override
            public void onChanged(@Nullable final LoginResponseModel loginResponseModel) {
                showProgress(false);
                PeriodicWorkRequest.Builder refreshTokenBuilder =new PeriodicWorkRequest.Builder(UserTokenRefreshJob.class, 15, TimeUnit.MINUTES);
                PeriodicWorkRequest request = refreshTokenBuilder.build();
                WorkManager.getInstance().enqueueUniquePeriodicWork("User Token refresh", ExistingPeriodicWorkPolicy.KEEP, request);
                startActivity(new Intent(LoginActivity.this, DetailActivity.class));
                finish();
            }
        });
        loginViewModel.getLoginErrorLiveData().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable final Throwable throwable) {
                throwable.printStackTrace();
                showProgress(false);

            }
        });
    }


    private void attemptLogin() {


        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        showProgress(true);
        loginViewModel.login(email, password);
    }


    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


}

