package com.example.scheduler.domain;

public enum Level {
    FRESHMAN("새내기"),
    DEATH_YEAR("사망년"),
    SENIOR("졸업반"),
    GRAD_STUDENT("대학원생"),
    ASSISTANT_PROFESSOR("조교수"),
    PROFESSOR("정교수"),
    PRESIDENT("총장");

    private final String displayName;

    Level(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
