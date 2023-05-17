package com.ll.rest.boundedContext.member.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @PostMapping("/member/login")
    public String login(){
        return "성공";
    }
}
