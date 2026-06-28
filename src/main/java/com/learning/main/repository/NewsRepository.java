package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.enm.NewsCategory;
import com.learning.main.model.News;
import java.time.LocalDate;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCategory(NewsCategory category);
    List<News> findByPublishDateBetween(LocalDate start, LocalDate end);
    List<News> findByAuthor(String author);
}
