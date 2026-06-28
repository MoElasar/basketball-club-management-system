package com.learning.main.repository.services;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class TrainingSessionService {
    @Autowired
    private TrainingSessionRepository trainingSessionRepository;

    public TrainingSession saveTrainingSession(TrainingSession trainingSession) {
        return trainingSessionRepository.save(trainingSession);
    }

    public Optional<TrainingSession> getTrainingSessionById(Long id) {
        return trainingSessionRepository.findById(id);
    }

    public List<TrainingSession> getAllTrainingSessions() {
        return trainingSessionRepository.findAll();
    }

    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.deleteById(id);
    }

    public List<TrainingSession> findTrainingSessionsBySessionDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        return trainingSessionRepository.findBySessionDateTimeBetween(start, end);
    }

    public List<TrainingSession> findTrainingSessionsByConductedBy(Staff staff) {
        return trainingSessionRepository.findByConductedBy(staff);
    }

    public List<TrainingSession> findTrainingSessionsByFocusArea(String focusArea) {
        return trainingSessionRepository.findByFocusArea(focusArea);
    }
}
