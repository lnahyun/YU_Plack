package com.example.scheduler.service;

import com.example.scheduler.dto.UserProfileResponseDto;
import com.example.scheduler.repository.UserRepository;
import com.example.scheduler.domain.User;
import com.example.scheduler.domain.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public User register(String username, String password, String email) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 사용 중인 사용자 이름입니다.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        if (password.length() < 6 || password.length() > 15) {
            throw new RuntimeException("비밀번호는 6자 이상, 15자 이하로 입력해야 합니다.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setScore(0);
        user.setCreatedAt(LocalDate.now());
        user.setLevel(Level.FRESHMAN);

        return userRepository.save(user);
    }


    // 로그인
    public User login(String username, String password) {
        // 입력값 누락 체크
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("이름을 입력해주세요.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("비밀번호를 입력해주세요.");
        }

        // 사용자 조회
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // 사용자 존재 여부 및 비밀번호 검증
        if (optionalUser.isEmpty() ||
                !optionalUser.get().getPassword().equals(password)) {
            throw new RuntimeException("계정 정보가 올바르지 않습니다.");
        }

        return optionalUser.get();
    }


    //점수추가 / 레벨 update
    public void addScore(User user, int points) {
        user.setScore(user.getScore() + points);
        updateLevel(user);
        userRepository.save(user);
    }

    //연속등록일수
    public void increaseStreak(User user) {
        user.setStreak(user.getStreak() + 1); // streak 필드가 필요함
        userRepository.save(user);
    }

    //레벨 업데이트
    private void updateLevel(User user) {
        int score = user.getScore();

        if (score >= 4000) user.setLevel(Level.PRESIDENT);
        else if (score >= 2000) user.setLevel(Level.PROFESSOR);
        else if (score >= 1000) user.setLevel(Level.ASSISTANT_PROFESSOR);
        else if (score >= 600)  user.setLevel(Level.GRAD_STUDENT);
        else if (score >= 300)  user.setLevel(Level.SENIOR);
        else if (score >= 100)  user.setLevel(Level.DEATH_YEAR);
        else                    user.setLevel(Level.FRESHMAN);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        return new UserProfileResponseDto(
                user.getUsername(),
                user.getLevel().getDisplayName(),
                user.getScore(),
                user.getStreak()
        );
    }
}
