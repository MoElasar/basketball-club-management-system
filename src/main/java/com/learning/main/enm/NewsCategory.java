package com.learning.main.enm;

public enum NewsCategory {
    TEAM_NEWS("Team News"),
    PLAYER_NEWS("Player News"),
    MATCH_REPORT("Match Report"),
    TRANSFER_NEWS("Transfer News"),
    COMMUNITY("Community");

    private final String displayName;

    NewsCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}