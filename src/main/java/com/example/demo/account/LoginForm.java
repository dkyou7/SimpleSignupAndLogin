package com.example.demo.account;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
    private String redirect_url;

    public LoginForm(String redirect_url){
        this.redirect_url = redirect_url;
    }
}
