package com.example.scheduler.controller;


import com.example.scheduler.domain.Level;
import com.example.scheduler.domain.User;
import com.example.scheduler.dto.TodoRequestDto;
import com.example.scheduler.dto.TodoResponseDto;
import com.example.scheduler.dto.UserProfileResponseDto;
import com.example.scheduler.dto.UserSignupDto;

import com.example.scheduler.service.TodoService;
import com.example.scheduler.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TodoService todoService;


    //회원가입 페이지 렌더링
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    //회원가입 처리
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@ModelAttribute @Valid UserSignupDto dto,
                                           BindingResult bindingResult) {
        // 1. Validation 실패 시
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        // 2. 비밀번호 불일치 시
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            return ResponseEntity.badRequest().body("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        try {
            // 3. 회원가입 로직 실행
            userService.register(dto.getUsername(), dto.getPassword(), dto.getEmail());
            return ResponseEntity.ok("회원가입 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //로그인 페이지 렌더링
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    //로그인 처리 + 세션 저장
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            User user = userService.login(username, password);
            session.setAttribute("userId", user.getId()); // 세션 저장
            return "redirect:/users/home";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    //사용자 이름 중복 체크 (AJAX용 모달 응답)
    @GetMapping("/check")
    @ResponseBody
    public String checkUsername(@RequestParam String username) {
        if (username == null || username.trim().isEmpty()) {
            return "이름을 입력해주세요.";
        }

        boolean exists = userService.usernameExists(username);
        return exists ? "이미 사용 중인 이름입니다." : "사용 가능한 이름입니다.";
    }

    //프로필 페이지 렌더링 (PathVariable 기반)
    @GetMapping("/{userId}/profile")
    public String showProfile(@PathVariable Long userId, Model model) {
        UserProfileResponseDto profile = userService.getUserProfile(userId);
        model.addAttribute("profile", profile);
        return "profile";
    }

    //홈화면 렌더링 (세션 기반)
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/users/login";
        }

        LocalDate today = LocalDate.now();
        List<TodoResponseDto> todayTodos = todoService.getTodosByDate(userId, today);
        List<TodoResponseDto> unfinishedTodos = todoService.getUnfinishedTodos(userId, today);

        model.addAttribute("todayTodos", todayTodos);
        model.addAttribute("unfinishedTodos", unfinishedTodos);
        return "home";
    }

    //로그아웃 처리
    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }


    //프로필 페이지 렌더링 (세션 기반, 진행도 포함 계산)
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/users/login";

        User user = userService.getUserById(userId); // ← 이 메서드 필요
        int score = user.getScore();
        Level level = user.getLevel();
        int min = level.getMinScore();
        int max = level.getMaxScore();
        int progressPercent = (int)(((double)(score - min) / (max - min)) * 100);

        model.addAttribute("user", user);
        model.addAttribute("progressPercent", progressPercent);
        return "profile";
    }



}
