package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Staff;
import com.learning.main.model.StaffContract;

import java.time.LocalDate;
import java.util.List;

public interface StaffContractRepository extends JpaRepository<StaffContract, Long> {
    List<StaffContract> findByStaff(Staff staff);
    List<StaffContract> findByStartDateAfter(LocalDate date); // Added this method 
    List<StaffContract> findByEndDateBefore(LocalDate date); // Added this method 
}