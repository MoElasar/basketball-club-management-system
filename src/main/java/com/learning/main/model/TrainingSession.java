package com.learning.main.model;

import java.time.LocalDateTime;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//This class represents a core business entity, holding data and relationships relevant to training sessions.
	// Data Mapper Pattern (via JPA annotations)
	// JPA annotations are used to map this object to the 'training_sessions' table in the database.
	
@Entity
@Table(name = "training_sessions")
public class TrainingSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sessionId;

	private String sessionName;
	private LocalDateTime sessionDateTime;
	private String focusArea;
	private String duration;
	private String location;

	@ManyToOne
	@JoinColumn(name = "staff_id")
	private Staff conductedBy;

	@ManyToMany
	@JoinTable(name = "player_training", joinColumns = @JoinColumn(name = "session_id"), inverseJoinColumns = @JoinColumn(name = "player_id"))
	private Set<Player> attendees;

	public TrainingSession() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public LocalDateTime getSessionDateTime() {
		return sessionDateTime;
	}

	public void setSessionDateTime(LocalDateTime sessionDateTime) {
		this.sessionDateTime = sessionDateTime;
	}

	public String getFocusArea() {
		return focusArea;
	}

	public void setFocusArea(String focusArea) {
		this.focusArea = focusArea;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Staff getConductedBy() {
		return conductedBy;
	}

	public void setConductedBy(Staff conductedBy) {
		this.conductedBy = conductedBy;
	}

	public Set<Player> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<Player> attendees) {
		this.attendees = attendees;
	}

}
