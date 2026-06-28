package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Staff;
import com.learning.main.model.TrainingSession;
import java.time.LocalDateTime;
import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
	List<TrainingSession> findBySessionDateTimeBetween(LocalDateTime start, LocalDateTime end);

	List<TrainingSession> findByConductedBy(Staff staff);

	List<TrainingSession> findByFocusArea(String focusArea);
}