package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Facility;
import com.learning.main.model.FacilityBooking;
import com.learning.main.model.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface FacilityBookingRepository extends JpaRepository<FacilityBooking, Long> {
	List<FacilityBooking> findByFacility(Facility facility);

	List<FacilityBooking> findByBookedBy(User user);

	List<FacilityBooking> findByBookingDateAndStartTimeBetween(LocalDate date, LocalTime start, LocalTime end);
}
