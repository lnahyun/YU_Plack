package com.example.scheduler.controller;

import com.example.scheduler.dto.TodoRequestDto;
import com.example.scheduler.dto.TodoResponseDto;
import com.example.scheduler.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // 일정 추가
    @PostMapping("/{userId}")
    public ResponseEntity<TodoResponseDto> createTodo(@PathVariable Long userId, @RequestBody TodoRequestDto requestDto) {
        TodoResponseDto response = todoService.createTodo(userId, requestDto);
        return ResponseEntity.ok(response);
    }


    // 일정 완료 처리
    @PutMapping("/{todoId}/complete")
    public ResponseEntity<Void> markAsDone(@PathVariable Long todoId) {
        todoService.markAsDone(todoId);
        return ResponseEntity.ok().build();
    }

    // 특정 날짜 기준 전체 일정 조회
    @GetMapping("/{userId}/{date}")
    public ResponseEntity<List<TodoResponseDto>> getTodosByDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
//        System.out.println("✅ [컨트롤러] 요청 도착 - userId: " + userId + ", date: " + date);

        List<TodoResponseDto> todos = todoService.getTodosByDate(userId, date);
        return ResponseEntity.ok(todos);
    }

    // 미완료 일정 조회
    @GetMapping("/{userId}/pending")
    public ResponseEntity<List<TodoResponseDto>> getPendingTodos(@PathVariable Long userId) {
        List<TodoResponseDto> todos = todoService.getPendingTodos(userId);
        return ResponseEntity.ok(todos);
    }

    // 일정 내용 수정
    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long todoId,
            @RequestBody TodoRequestDto requestDto
    ) {
        TodoResponseDto response = todoService.updateTodo(todoId, requestDto);
        return ResponseEntity.ok(response);
    }


    // 일정 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.noContent().build();
    }
}
