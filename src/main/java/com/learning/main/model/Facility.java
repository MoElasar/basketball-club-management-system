package com.learning.main.model;
import java.util.Set;

import com.learning.main.enm.FacilityType;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
@Entity
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;
    
    private String facilityName;
    private String location;
    
    @Enumerated(EnumType.STRING)
    private FacilityType type;
    
    private int capacity;
    private String description;
    
    @OneToMany(mappedBy = "facility")
    private Set<FacilityBooking> bookings;

	public Long getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public FacilityType getType() {
		return type;
	}

	public void setType(FacilityType type) {
		this.type = type;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<FacilityBooking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<FacilityBooking> bookings) {
		this.bookings = bookings;
	}
    
   
    
    
}