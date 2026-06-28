package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.main.enm.FacilityType;
import com.learning.main.model.Facility;


import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByType(FacilityType type);
    List<Facility> findByCapacityGreaterThanEqual(int capacity);
}
