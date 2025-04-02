package com.example.CalendarDevelop.cys.service;

import com.example.CalendarDevelop.cys.dto.request.ScheduleSaveRequestDto;
import com.example.CalendarDevelop.cys.dto.request.ScheduleUpdateRequestDto;
import com.example.CalendarDevelop.cys.dto.response.ScheduleResponseDto;
import com.example.CalendarDevelop.cys.entity.Member;
import com.example.CalendarDevelop.cys.entity.Schedule;
import com.example.CalendarDevelop.cys.repository.MemberRepository;
import com.example.CalendarDevelop.cys.repository.ScheduleRepository;
import com.example.CalendarDevelop.exception.ApplicationException;
import com.example.CalendarDevelop.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ScheduleResponseDto save(Long memberId, ScheduleSaveRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        Schedule schedule = new Schedule(
                requestDto.getTitle(),
                requestDto.getContent(),
                member
        );

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule);
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("해당 id의 일정이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        return new ScheduleResponseDto(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> findByMemberId(Long memberId) {
        return scheduleRepository.findByMemberId(memberId).stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduleResponseDto update(Long id, Long memberId, ScheduleUpdateRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("해당 id의 일정이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        // Check if the member is the owner of the schedule
        if (!schedule.getMember().getId().equals(memberId)) {
            throw new ApplicationException("일정을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        schedule.update(requestDto.getTitle(), requestDto.getContent());

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void delete(Long id, Long memberId, String password) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("해당 id의 일정이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        // Check if the member is the owner of the schedule
        if (!schedule.getMember().getId().equals(memberId)) {
            throw new ApplicationException("일정을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // Verify password
        if (!password.equals(member.getPassword())) {
            throw new PasswordMismatchException();
        }

        scheduleRepository.deleteById(id);
    }
}