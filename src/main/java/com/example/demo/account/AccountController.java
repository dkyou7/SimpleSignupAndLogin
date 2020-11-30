package com.example.demo.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm",new SignUpForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            return "sign-up";
        }
        accountService.signUp(signUpForm);
        return "redirect:/";
    }


    /**
     * 0. 먼저, 회원가입 수행한다.
     * 1. http://localhost:8080/go_login?redirect_url=http://localhost:8181
     * 으로 요청이 오면 처리하는 로직
     */
    @GetMapping("/go_login")
    public String generateCodeDisp(String redirect_url, Model model){
        model.addAttribute(new LoginForm(redirect_url));
        return "generate-code-login";
    }

    /**
     * 2. code = 값 반환한다.
     */
    @PostMapping("/generateCode")
    public String codePrint(LoginForm loginForm, Model model){
        Account byUsername = accountRepository.findByUsername(loginForm.getUsername());
        if(byUsername == null){
            throw new UsernameNotFoundException(loginForm.getUsername());
        }
        accountService.mergeInfo(byUsername,loginForm);
        return "redirect:"+loginForm.getRedirect_url()+"/response?code="+byUsername.getCode();
    }
    /**
     * 3. code값 받아서 처리하는 로직
     * http://localhost:8080/loginSuccess?code=dc70e062-33ec-4e08-979d-bb02fb363d74
     */
    @GetMapping("/loginSuccess")
    public String loginSuccess(String code, Model model){
        Account account = accountRepository.findByCode(code);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),   // 현재 인증된 사용자 정보 참조
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
        model.addAttribute("username",account.getUsername());
//        return "redirect:/";
        return "redirect:"+account.getRedirect_url()+"/sso=ok";
    }


    @GetMapping("/log-in")
    public String logInDisp(String redirect_url, Model model){
        model.addAttribute(new LoginForm(redirect_url));
        return "log-in";
    }
    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if(account != null){
            model.addAttribute(account);
        }

        return "index";
    }
}
