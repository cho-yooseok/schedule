package com.example.CalendarDevelop.cys.repository;

import com.example.CalendarDevelop.cys.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByMemberId(Long memberId);
}