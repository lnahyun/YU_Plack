package com.example.scheduler.dto;

import com.example.scheduler.domain.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequestDto {
    private String content;
    private String dueDate;
}