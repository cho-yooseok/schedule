package com.example.CalendarDevelop.cys.repository;

import com.example.CalendarDevelop.cys.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}