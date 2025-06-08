package com.example.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private int score;
    private int streak;

    @Enumerated(EnumType.STRING)
    private Level level;

    private LocalDate createdAt;
}
