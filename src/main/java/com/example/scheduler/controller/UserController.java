    package com.example.scheduler.controller;

    import com.example.scheduler.domain.User;
    import com.example.scheduler.dto.UserLoginDto;
    import com.example.scheduler.dto.UserProfileResponseDto;
    import com.example.scheduler.dto.UserSignupDto;
    import com.example.scheduler.service.UserService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("api/users")
    public class UserController {

        private final UserService userService;

        // 회원가입
        @PostMapping("/signup")
        public ResponseEntity<User> signup(@RequestBody @Valid UserSignupDto dto) {
            User user = userService.register(dto.getUsername(), dto.getPassword(), dto.getEmail());
            return ResponseEntity.ok(user);
        }

        // 로그인
        @PostMapping("/login")
        public ResponseEntity<User> login(@RequestBody @Valid UserLoginDto dto) {
            User user = userService.login(dto.getUsername(), dto.getPassword());
            return ResponseEntity.ok(user);
        }

        //프로필 조회
        @GetMapping("/{userId}/profile")
        public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long userId) {
            UserProfileResponseDto profile = userService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        }

    }
