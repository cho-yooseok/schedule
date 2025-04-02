package com.example.CalendarDevelop.cys.controller;

import com.example.CalendarDevelop.cys.dto.request.MemberSaveRequestDto;
import com.example.CalendarDevelop.cys.dto.request.MemberUpdateRequestDto;
import com.example.CalendarDevelop.cys.dto.response.MemberResponseDto;
import com.example.CalendarDevelop.cys.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<MemberResponseDto> save(
            @Valid @RequestBody MemberSaveRequestDto requestDto
    ) {
        return ResponseEntity.ok(memberService.save(requestDto));
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findById(id));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(memberService.update(id, requestDto));
    }

    @DeleteMapping("/members/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam String password
    ) {
        memberService.delete(id, password);
    }
}