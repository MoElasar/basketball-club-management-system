package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.model.Sponsor;

import java.util.List;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    List<Sponsor> findByIndustry(String industry);
}