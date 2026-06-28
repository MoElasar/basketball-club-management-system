package com.learning.main.repository.services;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
@Service
public class FacilityBookingService {
	@Autowired
	private FacilityBookingRepository facilityBookingRepository;

	public FacilityBooking saveFacilityBooking(FacilityBooking facilityBooking) {
		return facilityBookingRepository.save(facilityBooking);
	}

	public Optional<FacilityBooking> getFacilityBookingById(Long id) {
		return facilityBookingRepository.findById(id);
	}

	public List<FacilityBooking> getAllFacilityBookings() {
		return facilityBookingRepository.findAll();
	}

	public void deleteFacilityBooking(Long id) {
		facilityBookingRepository.deleteById(id);
	}

	public List<FacilityBooking> findFacilityBookingsByFacility(Facility facility) {
		return facilityBookingRepository.findByFacility(facility);
	}

	public List<FacilityBooking> findFacilityBookingsByBookedBy(User user) {
		return facilityBookingRepository.findByBookedBy(user);
	}

	public List<FacilityBooking> findFacilityBookingsByBookingDateAndStartTimeBetween(LocalDate date, LocalTime start,
			LocalTime end) {
		return facilityBookingRepository.findByBookingDateAndStartTimeBetween(date, start, end);
	}
}
