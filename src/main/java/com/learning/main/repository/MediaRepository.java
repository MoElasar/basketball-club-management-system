package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.main.enm.MediaType;
import com.learning.main.model.Game;
import com.learning.main.model.Media;

import java.time.LocalDate;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByGame(Game game);
    List<Media> findByType(MediaType type);
    List<Media> findByPublishDateBetween(LocalDate start, LocalDate end);
}