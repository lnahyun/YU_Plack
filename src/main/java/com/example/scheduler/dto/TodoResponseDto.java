package com.example.scheduler.dto;

import com.example.scheduler.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {
    private Long id;
    private String content;
    private LocalDate dueDate;
    private boolean isCompleted;
    private LocalDateTime createdAt;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.content = todo.getContent();
        this.dueDate = todo.getDueDate();
        this.isCompleted = todo.isCompleted();
        this.createdAt = todo.getCreatedAt();
    }
}
