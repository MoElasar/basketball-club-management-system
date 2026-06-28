package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class SponsorService {
	@Autowired
	private SponsorRepository sponsorRepository;

	public Sponsor saveSponsor(Sponsor sponsor) {
		return sponsorRepository.save(sponsor);
	}

	public Optional<Sponsor> getSponsorById(Long id) {
		return sponsorRepository.findById(id);
	}

	public List<Sponsor> getAllSponsors() {
		return sponsorRepository.findAll();
	}

	public void deleteSponsor(Long id) {
		sponsorRepository.deleteById(id);
	}

	public List<Sponsor> findSponsorsByIndustry(String industry) {
		return sponsorRepository.findByIndustry(industry);
	}
}