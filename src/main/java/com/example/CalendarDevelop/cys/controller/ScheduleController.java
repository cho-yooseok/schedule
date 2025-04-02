package com.example.CalendarDevelop.cys.controller;

import com.example.CalendarDevelop.cys.dto.request.ScheduleSaveRequestDto;
import com.example.CalendarDevelop.cys.dto.request.ScheduleUpdateRequestDto;
import com.example.CalendarDevelop.cys.dto.response.ScheduleResponseDto;
import com.example.CalendarDevelop.cys.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/members/{memberId}/schedules")
    public ResponseEntity<ScheduleResponseDto> save(
            @PathVariable Long memberId,
            @Valid @RequestBody ScheduleSaveRequestDto requestDto
    ) {
        return ResponseEntity.ok(scheduleService.save(memberId, requestDto));
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @GetMapping("/members/{memberId}/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> findByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(scheduleService.findByMemberId(memberId));
    }

    @PutMapping("/members/{memberId}/schedules/{id}")
    public ResponseEntity<ScheduleResponseDto> update(
            @PathVariable Long id,
            @PathVariable Long memberId,
            @Valid @RequestBody ScheduleUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(scheduleService.update(id, memberId, requestDto));
    }

    @DeleteMapping("/members/{memberId}/schedules/{id}")
    public void delete(
            @PathVariable Long id,
            @PathVariable Long memberId,
            @RequestParam String password
    ) {
        scheduleService.delete(id, memberId, password);
    }
}