package com.learning.main.repository.services;
import com.learning.main.enm.FacilityType;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class FacilityService {
    @Autowired
    private FacilityRepository facilityRepository;

    public Facility saveFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    public Optional<Facility> getFacilityById(Long id) {
        return facilityRepository.findById(id);
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public void deleteFacility(Long id) {
        facilityRepository.deleteById(id);
    }

    public List<Facility> findFacilitiesByType(FacilityType type) {
        return facilityRepository.findByType(type);
    }

    public List<Facility> findFacilitiesByCapacityGreaterThanEqual(int capacity) {
        return facilityRepository.findByCapacityGreaterThanEqual(capacity);
    }
}