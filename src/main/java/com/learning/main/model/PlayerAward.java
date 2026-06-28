package com.learning.main.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

// This class represents an award received by a player.
@Entity
@Table(name = "player_awards")
public class PlayerAward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long awardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private String awardName;

    @Column(nullable = false)
    private LocalDate awardDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean isTeamAward; // True if it's a team award, false if individual

    // Getters and Setters
    public Long getAwardId() {
        return awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public LocalDate getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(LocalDate awardDate) {
        this.awardDate = awardDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTeamAward() {
        return isTeamAward;
    }

    public void setTeamAward(boolean teamAward) {
        isTeamAward = teamAward;
    }
}