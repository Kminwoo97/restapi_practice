package com.ll.rest.boundedContext.member.controller;


import com.ll.rest.base.rsData.RsData;
import com.ll.rest.boundedContext.member.entity.Member;
import com.ll.rest.boundedContext.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    @Operation(summary = "로그인, 엑세스 토큰 발급")
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                       HttpServletResponse response) {
        Member member = memberService
                .findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (member == null)
            return RsData.of("F-1", "존재하지 않는 회원입니다.");

        RsData rsData = memberService.canGenAccessToken(member, loginRequest.getPassword());

        if (rsData.isFail()) return rsData;

        String accessToken = memberService.genAccessToken(member, loginRequest.getPassword());


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
    @Operation(summary = "로그인된 사용자의 정보", security = @SecurityRequirement(name = "bearerAuth")) //Swagger 문서에만 의미있는 것
    public RsData<MeResponse> me(@AuthenticationPrincipal User user){
        //security 의 @AuthenticationPrincipal
        Member member = memberService.findByUsername("user1").get();

        return RsData.of(
                "S-1",
                "성공",
                new MeResponse(member)
        );
    }
}
