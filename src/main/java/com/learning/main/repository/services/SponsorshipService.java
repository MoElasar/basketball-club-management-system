package com.learning.main.repository.services;

import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class SponsorshipService {
	@Autowired
	private SponsorshipRepository sponsorshipRepository;

	public Sponsorship saveSponsorship(Sponsorship sponsorship) {
		return sponsorshipRepository.save(sponsorship);
	}

	public Optional<Sponsorship> getSponsorshipById(Long id) {
		return sponsorshipRepository.findById(id);
	}

	public List<Sponsorship> getAllSponsorships() {
		return sponsorshipRepository.findAll();
	}

	public void deleteSponsorship(Long id) {
		sponsorshipRepository.deleteById(id);
	}

	public List<Sponsorship> findSponsorshipsByTeam(Team team) {
		return sponsorshipRepository.findByTeam(team);
	}

	public List<Sponsorship> findSponsorshipsBySponsor(Sponsor sponsor) {
		return sponsorshipRepository.findBySponsor(sponsor);
	}

	public List<Sponsorship> findSponsorshipsByStartDateAfter(LocalDate date) {
		return sponsorshipRepository.findByStartDateAfter(date);
	}
}
