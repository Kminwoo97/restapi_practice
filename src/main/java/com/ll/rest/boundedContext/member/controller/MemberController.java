package com.ll.rest.boundedContext.member.controller;


import com.ll.rest.boundedContext.member.entity.Member;
import com.ll.rest.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    @Data
    static class LoginRequest{
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @PostMapping("/login")
    public Member login(@Valid @RequestBody LoginRequest loginRequest,
                        HttpServletResponse response) {
        //Header 값에서 JWT 토큰 받아와야한다.
        response.addHeader("Authentication", "JWT 토큰");

        return memberService.findByUsername(loginRequest.getUsername()).orElse(null);
    }
}