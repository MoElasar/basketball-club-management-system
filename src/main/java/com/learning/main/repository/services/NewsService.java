package com.learning.main.repository.services;

import com.learning.main.enm.NewsCategory;
import com.learning.main.model.News;
import com.learning.main.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

	@Autowired
	private NewsRepository newsRepository;

	public List<News> getAllNews() {
		return newsRepository.findAll();
	}

	public Optional<News> getNewsById(Long id) {
		return newsRepository.findById(id);
	}

	public News saveNews(News news) {
		return newsRepository.save(news);
	}

	public void deleteNews(Long id) {
		newsRepository.deleteById(id);
	}

	public List<News> getNewsByCategory(NewsCategory category) {
		return newsRepository.findByCategory(category);
	}

	public List<News> getNewsByPublishDateBetween(LocalDate startDate, LocalDate endDate) {
		return newsRepository.findByPublishDateBetween(startDate, endDate);
	}
}