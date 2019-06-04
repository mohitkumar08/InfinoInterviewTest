package com.bit.myapplication.repo.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "email",
        "password"
})
public final class LoginRequestModel {

    @JsonProperty("email")
    private final String email;
    @JsonProperty("password")
    private final String password;

    public LoginRequestModel(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }


    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

}