package com.s576air.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }

//    @PostMapping("/login")
//    public String handleLogin() {
//        // 로그인 여부 확인 및 처리
//        // 로그인 성공시 톡방 페이지 반환
//        // 로그인 실패시 페이지 반환
//    }

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
