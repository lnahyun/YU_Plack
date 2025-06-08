package com.example.scheduler.service;

import com.example.scheduler.domain.PointHistory;
import com.example.scheduler.domain.User;
import com.example.scheduler.dto.PointHistoryRequestDto;
import com.example.scheduler.dto.PointHistoryResponseDto;
import com.example.scheduler.repository.PointHistoryRepository;
import com.example.scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;

    // 포인트 변경 내역 저장
    @Transactional
    public void savePointHistory(Long userId, PointHistoryRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        PointHistory history = new PointHistory();
        history.setUser(user);
        history.setChangeAmount(requestDto.getChangeAmount());
        history.setReason(requestDto.getReason());
        history.setCreatedAt(LocalDateTime.now());

        pointHistoryRepository.save(history);
    }

    // 포인트 변경 내역 조회
    public List<PointHistoryResponseDto> getUserPointHistories(Long userId) {
        return pointHistoryRepository.findByUserId(userId).stream()
                .map(p -> new PointHistoryResponseDto(
                        p.getChangeAmount(),
                        p.getReason(),
                        p.getCreatedAt()
                )).collect(Collectors.toList());
    }
}
