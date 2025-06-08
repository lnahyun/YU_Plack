package com.example.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanType type; // WEEKLY or MONTHLY

    private LocalDate startDate;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_plan_user"))
    private User user;

    public enum PlanType {
        WEEKLY,
        MONTHLY
    }
}


