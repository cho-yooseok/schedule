package com.example.CalendarDevelop.cys.service;

import com.example.CalendarDevelop.cys.dto.request.LoginRequestDto;
import com.example.CalendarDevelop.cys.entity.Member;
import com.example.CalendarDevelop.cys.repository.MemberRepository;
import com.example.CalendarDevelop.exception.AuthenticationException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private static final String USER_SESSION_KEY = "LOGGED_IN_USER";

    @Transactional(readOnly = true)
    public Member login(LoginRequestDto requestDto, HttpSession session) {
        Member member = memberRepository.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword())
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // Save member ID in session
        session.setAttribute(USER_SESSION_KEY, member.getId());

        return member;
    }

    public void logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_KEY);
        session.invalidate();
    }

    public static Long getLoggedInUserId(HttpSession session) {
        Object attribute = session.getAttribute(USER_SESSION_KEY);
        if (attribute == null) {
            return null;
        }
        return (Long) attribute;
    }
}

//