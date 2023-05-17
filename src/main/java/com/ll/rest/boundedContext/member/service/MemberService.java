package com.ll.rest.boundedContext.member.service;


import com.ll.rest.base.jwt.JwtProvider;
import com.ll.rest.boundedContext.member.entity.Member;
import com.ll.rest.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }


    public String genAccessToken(String username, String password) {
        Member member = findByUsername(username).orElse(null);

        //올바르지 않은 사용자이면 null
        if (member == null)
            return null;

        //비밀번호 올바르지 않으면 null
        if (!passwordEncoder.matches(password, member.getPassword())) {
            return null;
        }

        //member 의 데이터를 받아서 그 데이터를 JWT 토큰으로 만들어서 반환
        return jwtProvider.genToken(member.toClaims(), 60 * 60 * 24 * 365);
    }
}

