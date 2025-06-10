package com.example.scheduler.service;

import com.example.scheduler.domain.Todo;
import com.example.scheduler.domain.User;
import com.example.scheduler.dto.TodoRequestDto;
import com.example.scheduler.dto.TodoResponseDto;
import com.example.scheduler.repository.TodoRepository;
import com.example.scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // 일정 생성 + 연속 등록 및 점수 계산
    @Transactional
    public TodoResponseDto createTodo(Long userId, TodoRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        LocalDate dueDate = requestDto.getDueDate();

        //오늘이 처음 등록인가?
        boolean isFirstTodoToday = todoRepository.findByUserIdAndDueDate(userId, dueDate).isEmpty();
        //어제 등록된 일정이 있는가?
        boolean hasTodoYesterday = !todoRepository.findByUserIdAndDueDate(userId, dueDate.minusDays(1)).isEmpty();

        //연속 등록 처리
        if (isFirstTodoToday) {
            if (hasTodoYesterday) {
                user.setStreak(user.getStreak() + 1);
            } else {
                user.setStreak(1);
            }
            userService.addScore(user, user.getStreak());
        }

        //일정 저장
        Todo todo = new Todo();
        todo.setUser(user);
        todo.setContent(requestDto.getContent());
        todo.setDueDate(dueDate);
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());

        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponseDto(savedTodo);
    }

    // 일정 완료 처리 + 점수 계산
    @Transactional
    public void markAsDone(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("해당 일정을 찾을 수 없습니다."));

        if (todo.isCompleted()) return;

        todo.setCompleted(true);

        User user = todo.getUser();

        //오늘 완료된 투두 개수에 따라 점수 결정
        long todayCompleted = todoRepository.findByUserIdAndDueDate(user.getId(), LocalDate.now())
                .stream().filter(Todo::isCompleted).count();

        int baseScore;
        if (todayCompleted <= 5) baseScore = 2;
        else if (todayCompleted <= 10) baseScore = 4;
        else baseScore = 6;

        int streak = user.getStreak();
        int totalScore = baseScore + streak;

        userService.addScore(user, totalScore);
    }

    // 일정 내용 및 날짜 수정
    @Transactional
    public TodoResponseDto updateTodo(Long todoId, TodoRequestDto requestDto) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("해당 일정을 찾을 수 없습니다."));

        todo.setContent(requestDto.getContent());
        todo.setDueDate(requestDto.getDueDate());
        return new TodoResponseDto(todo);
    }

    // 일정 삭제
    @Transactional
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }

    // 특정 날짜의 모든 일정 조회
    public List<TodoResponseDto> getTodosByDate(Long userId, LocalDate date) {
        return todoRepository.findByUserIdAndDueDate(userId, date).stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 미완료 일정 조회
    public List<TodoResponseDto> getPendingTodos(Long userId) {
        return todoRepository.findByUserIdAndIsCompletedFalse(userId).stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 홈 화면용 : 오늘/이전까지 완료되지 않은 일정 조회
    public List<TodoResponseDto> getUnfinishedTodos(Long userId, LocalDate today) {
        return todoRepository.findByUserIdAndIsCompletedFalseAndDueDateBefore(userId, today).stream()
                .map(todo -> new TodoResponseDto(todo))  // ← 이 방식으로 안전하게 통일!
                .collect(Collectors.toList());
    }

    //일정 완료 상태 토글
    public void toggleCompleted(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
    }


}
