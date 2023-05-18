package com.ll.rest.boundedContext.member.controller;


import com.ll.rest.base.rsData.RsData;
import com.ll.rest.boundedContext.member.entity.Member;
import com.ll.rest.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    @Data
    static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @AllArgsConstructor
    @Getter
    static class LoginResponse {
        private final String accessToken;
    }

    @PostMapping("/login")
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                       HttpServletResponse response) {
        //로그인 성공 시, JWT 토큰 생성
        String accessToken = memberService.genAccessToken(loginRequest.getUsername(), loginRequest.getPassword());

        //생성한 JWT 토큰을 Header 값에서 넣어준다.
        //response.addHeader("Authentication", accessToken);

        return RsData.of(
                "S-1",
                "엑세스토큰이 생성되었습니다.",
                new LoginResponse(accessToken)
        );
    }

    @Getter
    @AllArgsConstructor
    static class MeResponse{
        private final Member member;
    }

    //consumes = ALL_VALUE 는 입력 데이터 형태가 어떤 것이든 상관없다.

    @GetMapping(value = "/me", consumes = ALL_VALUE)
    public RsData<MeResponse> me(){
        //지금은 임의로 NotProd에 넣은 임의의 사용자를 넣었지만, 실제로는 로그인 한 사용자를 조회해야 한다.
        Member member = memberService.findByUsername("user1").get();

        return RsData.of(
                "S-1",
                "성공",
                new MeResponse(member)
        );
    }
}
