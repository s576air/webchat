package com.s576air.webchat.controller;

import com.s576air.webchat.dto.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }

    @GetMapping("/sign-up")
    public String signUpPage() {
        return "forward:/signUp.html";
    }

//    @PostMapping("/sign-up")
//    public String handleSignUp() {
//        // 회원가입 성공시 회원가입 성공 페이지로 이동
//        // 회원가입 실패시 그냥 실패
//        return "";
//    }
}
