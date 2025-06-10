package com.example.scheduler.controller;

import com.example.scheduler.dto.PointHistoryRequestDto;
import com.example.scheduler.dto.PointHistoryResponseDto;
import com.example.scheduler.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    //포인트 히스토리 조회
    @GetMapping("/{userId}")
    public String viewPointHistories(@PathVariable Long userId, Model model) {
        List<PointHistoryResponseDto> histories = pointHistoryService.getUserPointHistories(userId);
        model.addAttribute("pointHistories", histories);
        return "points"; // templates/points.html
    }

    //포인트 히스토리 저장
    @PostMapping("/{userId}")
    public String savePointHistory(@PathVariable Long userId, @ModelAttribute PointHistoryRequestDto dto) {
        pointHistoryService.savePointHistory(userId, dto);
        return "redirect:/points/" + userId;
    }
}