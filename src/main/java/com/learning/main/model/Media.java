package com.learning.main.model;
import java.time.LocalDate;

import com.learning.main.enm.MediaType;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;
    
    private String title;
    
    @Enumerated(EnumType.STRING)
    private MediaType type;
    
    private String url;
    private LocalDate publishDate;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    
    // Constructors, getters, setters
}
