package com.example.scheduler.controller;

import com.example.scheduler.domain.User;
import com.example.scheduler.dto.TodoRequestDto;
import com.example.scheduler.dto.TodoResponseDto;
import com.example.scheduler.service.TodoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.TreeMap;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    //미완료 to-do list 조회 (날짜별 그룹화)
    @GetMapping("/pending")
    public String pendingTodos(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        List<TodoResponseDto> todos = todoService.getPendingTodos(userId);

        // 날짜별로 그룹화
        Map<LocalDate, List<TodoResponseDto>> grouped = todos.stream()
                .collect(Collectors.groupingBy(TodoResponseDto::getDueDate,
                        TreeMap::new, Collectors.toList()));

        model.addAttribute("pendingTodos", grouped);
        model.addAttribute("today", LocalDate.now());
        return "pending";
    }

    //to-do 완료 상태 토글
    @PostMapping("/toggle/{todoId}")
    public String toggleTodo(@PathVariable Long todoId,
                             @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        todoService.toggleCompleted(todoId);
        return "redirect:/todos/view?date=" + date;
    }

    //to-do 내용 수정
    @PostMapping("/update/{todoId}")
    public String updateTodo(@PathVariable Long todoId, @ModelAttribute TodoRequestDto requestDto) {
        todoService.updateTodo(todoId, requestDto);
        return "redirect:/todos/view?date=" + requestDto.getDueDate();
    }

    //to-do 삭제
    @GetMapping("/delete/{todoId}")
    public String deleteTodo(@PathVariable Long todoId,
                             @RequestParam(name = "date", required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             HttpSession session) {
        todoService.deleteTodo(todoId);
        if (date == null) {
            date = (LocalDate) session.getAttribute("lastViewedDate");
            if (date == null) date = LocalDate.now();
        }
        return "redirect:/todos/view?date=" + date;
    }

    //선택 날짜 투두 조회 (세션)
    @GetMapping("/view")
    public String getTodosFromSession(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            HttpSession session,
            Model model) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        if (date == null) {
            date = LocalDate.now(); // 기본값 처리
        }

        session.setAttribute("lastViewedDate", date);
        List<TodoResponseDto> todos = todoService.getTodosByDate(userId, date);

        model.addAttribute("todos", todos);
        model.addAttribute("date", date);
        return "todo";
    }

    //새로운 to-do 생성 (세션)
    @PostMapping("/add")
    public String createTodoSession(HttpSession session,
                                    @ModelAttribute TodoRequestDto requestDto) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        todoService.createTodo(userId, requestDto);
        return "redirect:/todos/view?date=" + requestDto.getDueDate();
    }
}
