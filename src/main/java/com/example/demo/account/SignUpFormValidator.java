package com.example.demo.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);   // 어떤 타입의 인스턴스를 검증을 할 것인가?
    }

    // 뭘 검사할 것인가?
    @Override
    public void validate(Object o, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)o;
        if(accountRepository.existsByUsername(signUpForm.getUsername())){
            errors.rejectValue("username","invalid username",new Object[]{signUpForm.getUsername()},"이미 사용중인 아이디입니다.");
        }
    }
}
