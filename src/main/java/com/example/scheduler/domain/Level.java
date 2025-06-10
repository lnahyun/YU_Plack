package com.example.scheduler.domain;

public enum Level {
    FRESHMAN("새내기", 0, 99),
    DEATH_YEAR("사망년", 100, 299),
    SENIOR("졸업반", 300, 599),
    GRAD_STUDENT("대학원생", 600, 999),
    ASSISTANT_PROFESSOR("조교수", 1000, 1999),
    PROFESSOR("정교수", 2000, 3999),
    PRESIDENT("총장", 4000, Integer.MAX_VALUE);

    private final String displayName;
    private final int minScore;
    private final int maxScore;

    // constructor
    Level(String displayName, int minScore, int maxScore) {
        this.displayName = displayName;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public String getDisplayName() { return displayName; }
    public int getMinScore() { return minScore; }
    public int getMaxScore() { return maxScore; }
}

