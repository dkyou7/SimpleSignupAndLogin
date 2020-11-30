package com.example.demo.account;

import lombok.Data;

@Data
public class SignUpForm {
    private String username;
    private String password;
    private String password_repeat;
    private boolean remember;
}
