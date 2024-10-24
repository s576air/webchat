package com.s576air.webchat.controller;

import com.s576air.webchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }

    @GetMapping("/sign-up")
    public String signUpPage() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String handleSignUp(
        @RequestParam("id") String id,
        @RequestParam("password") String password,
        @RequestParam("password2") String password2,
        Model model
    ) {
        if (id == null || password == null || password2 == null) {
            model.addAttribute("err", "아이디가 올바르지 않습니다.");
            return "sign-up";
        }
        if (!password.equals(password2)) {
            model.addAttribute("err", "비밀번호가 다릅니다.");
            return "sign-up";
        }
        if (userService.signUp(id, password)) {
            return "redirect:/sign-up-success.html";
        } else {
            model.addAttribute("err", "이미 존재하는 아이디입니다.");
            return "sign-up";
        }
    }
}
