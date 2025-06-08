package com.example.scheduler.service;

import com.example.scheduler.domain.PointHistory;
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

    // 일정 생성
    @Transactional
    public TodoResponseDto createTodo(Long userId, TodoRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        LocalDate dueDate = LocalDate.parse(requestDto.getDueDate());

        // ✅ 오늘 해당 유저가 등록한 일정이 없을 경우만 streak 보상
        boolean isFirstTodoToday = todoRepository.findByUserIdAndDueDate(userId, dueDate).isEmpty();

        // ✅ streak 관리: 어제 일정 등록 여부 체크
        boolean hasTodoYesterday = !todoRepository.findByUserIdAndDueDate(userId, dueDate.minusDays(1)).isEmpty();

        if (isFirstTodoToday) {
            if (hasTodoYesterday) {
                user.setStreak(user.getStreak() + 1);
            } else {
                user.setStreak(1); // streak 초기화 후 시작
            }

            // ✅ streak 점수 부여
            userService.addScore(user, user.getStreak());
        }

        // 일정 저장
        Todo todo = new Todo();
        todo.setUser(user);
        todo.setContent(requestDto.getContent());
        todo.setDueDate(dueDate);
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());

        Todo savedTodo = todoRepository.save(todo);

        return new TodoResponseDto(savedTodo);
    }



    // 일정 완료 처리
    @Transactional
    public void markAsDone(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("해당 일정을 찾을 수 없습니다."));

        if (todo.isCompleted()) return;

        todo.setCompleted(true);

        User user = todo.getUser();

        // 1. 오늘 완료 일정 수 계산
        long todayCompleted = todoRepository.findByUserIdAndDueDate(user.getId(), LocalDate.now())
                .stream()
                .filter(Todo::isCompleted)
                .count();

        int baseScore;
        if (todayCompleted <= 5) baseScore = 2;
        else if (todayCompleted <= 10) baseScore = 4;
        else baseScore = 6;

        // 2. streak 점수 추가
        int streak = user.getStreak(); // 연속 등록일 수
        int totalScore = baseScore + streak;

        // 3. 점수 및 레벨 반영
        userService.addScore(user, totalScore);
    }


    // 일정 수정
    @Transactional
    public TodoResponseDto updateTodo(Long todoId, TodoRequestDto requestDto) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("해당 일정을 찾을 수 없습니다."));

        todo.setContent(requestDto.getContent());
        todo.setDueDate(LocalDate.parse(requestDto.getDueDate()));
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

    // 미완료 일정 조회 (날짜별 그룹핑)
    public List<TodoResponseDto> getPendingTodos(Long userId) {
        return todoRepository.findByUserIdAndIsCompletedFalse(userId).stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }
}

