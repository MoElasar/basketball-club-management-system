package com.learning.main.repository.services;

import com.learning.main.enm.MediaType;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
	@Autowired
	private MediaRepository mediaRepository;

	public Media saveMedia(Media media) {
		return mediaRepository.save(media);
	}

	public Optional<Media> getMediaById(Long id) {
		return mediaRepository.findById(id);
	}

	public List<Media> getAllMedia() {
		return mediaRepository.findAll();
	}

	public void deleteMedia(Long id) {
		mediaRepository.deleteById(id);
	}

	public List<Media> findMediaByGame(Game game) {
		return mediaRepository.findByGame(game);
	}

	public List<Media> findMediaByType(MediaType type) {
		return mediaRepository.findByType(type);
	}

	public List<Media> findMediaByPublishDateBetween(LocalDate start, LocalDate end) {
		return mediaRepository.findByPublishDateBetween(start, end);
	}
}