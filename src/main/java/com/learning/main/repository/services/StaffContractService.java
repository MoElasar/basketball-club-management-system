package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class StaffContractService {
	@Autowired
	private StaffContractRepository staffContractRepository;

	public StaffContract saveStaffContract(StaffContract staffContract) {
		return staffContractRepository.save(staffContract); 
	}

	public Optional<StaffContract> getStaffContractById(Long id) {
		return staffContractRepository.findById(id);
	}

	public List<StaffContract> getAllStaffContracts() {
		return staffContractRepository.findAll();
	}

	public void deleteStaffContract(Long id) {
		staffContractRepository.deleteById(id); 
	}

	public List<StaffContract> findStaffContractsByStaff(Staff staff) {
		return staffContractRepository.findByStaff(staff);
	}

	public List<StaffContract> findStaffContractsByStartDateAfter(LocalDate date) {
		return staffContractRepository.findByStartDateAfter(date);
	}

	public List<StaffContract> findStaffContractsByEndDateBefore(LocalDate date) {
		return staffContractRepository.findByEndDateBefore(date);
	}
}