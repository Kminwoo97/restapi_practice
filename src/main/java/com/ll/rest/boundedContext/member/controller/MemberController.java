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
    public String login(@Valid @RequestBody LoginRequest loginRequest,
                        HttpServletResponse response) {
        //로그인 성공 시, JWT 토큰 생성
        String accessToken = memberService.genAccessToken(loginRequest.getUsername(), loginRequest.getPassword());

        //생성한 JWT 토큰을 Header 값에서 넣어준다.
        response.addHeader("Authentication", accessToken);

        return "응답 본문";
    }
}
