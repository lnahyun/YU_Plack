package com.example.scheduler.repository;

import com.example.scheduler.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserIdAndDueDate(Long userId, LocalDate dueDate);
    List<Todo> findByUserIdAndIsCompletedFalse(Long userId);
}
