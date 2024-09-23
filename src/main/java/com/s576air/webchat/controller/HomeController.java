package com.s576air.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectUserOrGuest() {
        // 만약 로그인 상태면, "redirect:/chatRoomsPage"
        // 로그인 상태가 아니라면,
        return "redirect:/login";
    }
}
