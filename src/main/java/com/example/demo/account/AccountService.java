package com.example.demo.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpForm signUpForm) {
        Account account = Account.builder().username(signUpForm.getUsername())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .password_repeat(signUpForm.getPassword_repeat())
                .build();
        accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException(username);
        }

        return new UserAccount(account);
    }


    public void mergeInfo(Account byUsername, LoginForm loginForm) {
        byUsername.generateCode();
        byUsername.setRedirect_url(loginForm.getRedirect_url());
    }
}
